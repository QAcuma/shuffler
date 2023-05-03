package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.exception.TelegramApiException;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TRender;
import ru.acuma.shuffler.service.telegram.ExecuteService;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RenderService {

    private final EventContext eventContext;
    private final MessageService messageService;
    private final KeyboardService keyboardService;
    private final ExecuteService executeService;

    public void render(final Long chatId) {
        Optional.ofNullable(eventContext.findEvent(chatId))
            .filter(event -> !Objects.equals(event.getHash(), event.hashCode()))
            .ifPresent(event -> {
                event.getMessages()
                        .stream()
                        .filter(TRender::requireChanges)
                        .forEach(render -> {
                            executeMethod(event, render);
                            executeAfterActions(chatId, render);
                        });
                event.getMessages()
                    .removeIf(message -> Objects.isNull(message.getMessageType()));
                }
            );
    }

    public void delete(final Long chatId, final TRender render) {
        var method = messageService.deleteMessage(chatId, render.getMessageId());
        executeService.execute(method, render);
    }

    private void executeMethod(
        final TEvent event,
        final TRender render
    ) {
        var method = resolveApiMethod(render, event);
        switch (render.getExecuteStrategy()) {
            case REGULAR -> executeService.execute(method, render);
            case DELAYED -> executeService.executeLater(method, render);
            case TIMER -> executeTimedMarkup(method, event, render);
            case IDLE -> render.success();
        }
    }

    private BotApiMethod<?> resolveApiMethod(
        final TRender render,
        final TEvent event
    ) {
        var messageType = render.getMessageType();

        return switch (render.getMessageAction()) {
            case SEND -> messageService.buildMessage(event, messageType);
            case UPDATE -> messageService.buildMessageUpdate(event, render.getMessageId(), messageType);
            case UPDATE_MARKUP -> messageService.buildReplyMarkupUpdate(event, render.getMessageId(), messageType);
            case DELETE -> messageService.deleteMessage(event.getChatId(), render.getMessageId());
        };
    }

    private void executeAfterActions(final Long chatId, final TRender render) {
        render.getAfterActions().forEach(
            afterAction -> {
                var method = switch (afterAction) {
                    case PIN -> messageService.pinMessage(chatId, render.getMessageId());
                };
                executeService.execute(method, render);
            }
        );
    }

    public <T extends Serializable, M extends BotApiMethod<T>> void executeTimedMarkup(
        final M method,
        final TEvent event,
        final TRender render
    ) {
        var message = Optional.of(executeService.execute(method, render))
            .filter(Message.class::isInstance)
            .map(Message.class::cast)
            .orElseThrow(() -> new TelegramApiException(ExceptionCause.EXTRACT_RESPONSE_MESSAGE));

        IntStream.rangeClosed(1, Constants.DISABLED_BUTTON_TIMEOUT)
            .forEach(delay -> {
                var keyboard = keyboardService.getTimedKeyboard(Constants.DISABLED_BUTTON_TIMEOUT - delay);
                executeService.executeLater(
                    messageService.buildReplyMarkupUpdate(event, message.getMessageId(), keyboard),
                    TRender.forMarkup(MessageType.CHECKING_TIMED, message.getMessageId()).withDelay(delay));
            });
    }
}
