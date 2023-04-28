package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.telegram.ExecuteService;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RenderService {

    private final EventContext eventContext;
    private final MessageService messageService;
    private final KeyboardService keyboardService;
    private final ExecuteService executeService;

    public void render(final Long chatId) {
        Optional.ofNullable(eventContext.findEvent(chatId))
            .ifPresent(event -> Stream.concat(
                    event.getMessages().entrySet().stream(),
                    event.getDeletes().entrySet().stream())
                .filter(entry -> entry.getValue().requireChanges())
                .forEach(entry -> {
                    executeMethod(event, entry.getKey(), entry.getValue());
                    executeAfterActions(chatId, entry.getValue());
                })
            );
    }

    private void executeMethod(
        final TEvent event,
        final MessageType messageType,
        final Render render
    ) {
        var method = resolveApiMethod(messageType, render, event);
        switch (render.getExecuteStrategy()) {
            case REGULAR -> executeService.execute(method, render);
            case DELAYED -> executeService.executeLater(method, render);
            case SCHEDULED -> executeTimer(method, event, render);
            case IDLE -> render.success();
        }
    }

    private BotApiMethod<?> resolveApiMethod(
        final MessageType messageType,
        final Render render,
        final TEvent event
    ) {
        return switch (render.getMessageAction()) {
            case SEND -> messageService.buildMessage(event, messageType);
            case UPDATE -> messageService.buildMessageUpdate(event, render.getMessageId(), messageType);
            case UPDATE_MARKUP -> messageService.buildReplyMarkupUpdate(event, render.getMessageId(), messageType);
            case DELETE -> messageService.deleteMessage(event, render.getMessageId());
        };
    }

    private void executeAfterActions(final Long chatId, final Render render) {
        render.getAfterActions().forEach(
            afterAction -> {
                var method = switch (afterAction) {
                    case PIN -> messageService.pinMessage(chatId, render.getMessageId());
                };
                executeService.execute(method, render);
            }
        );
    }

    public <T extends Serializable, M extends BotApiMethod<T>> void executeTimer(
        final M method,
        final TEvent event,
        final Render render
    ) {
        var message = Optional.of(executeService.execute(method, Render.forSend()))
            .filter(Message.class::isInstance)
            .map(Message.class::cast)
            .orElseThrow();

        IntStream.rangeClosed(1, Constants.DISABLED_BUTTON_TIMEOUT)
            .mapToObj(delay -> keyboardService.getTimedKeyboard(Constants.DISABLED_BUTTON_TIMEOUT - delay))
            .map(keyboard -> messageService.buildReplyMarkupUpdate(event, message.getMessageId(), keyboard))
            .forEach(keyboardMethod -> executeService.executeLater(keyboardMethod, render));
    }
}
