package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.exception.TelegramApiException;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.domain.Render;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.telegram.ExecuteService;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

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
                        .filter(Render::requireChanges)
                        .forEach(render -> {
                            executeMethod(event, render);
                            executeAfterActions(event, render);
                        });
                    event.getMessages()
                        .removeIf(message -> Objects.isNull(message.getMessageType()));
                }
            );
    }

    public void delete(final Long chatId, final Render render) {
        var method = messageService.deleteMessage(chatId, render.getMessageId());
        executeService.execute(method, render);
    }

    private void executeMethod(
        final TEvent event,
        final Render render
    ) {
        var method = resolveApiMethod(render, event);
        switch (render.getExecuteStrategy()) {
            case REGULAR -> executeService.execute(method, render);
            case DELAYED -> executeService.executeLater(method, render);
            case TIMER -> executeTimedMarkup(method, render);
            case IDLE -> render.success();
        }
    }

    private BotApiMethod<?> resolveApiMethod(
        final Render render,
        final TEvent event
    ) {
        var messageType = render.getMessageType();

        return switch (render.getMessageAction()) {
            case SEND -> messageService.sendMessage(event, messageType);
            case UPDATE -> messageService.buildMessageUpdate(event, render.getMessageId(), messageType);
            case UPDATE_MARKUP -> messageService.buildReplyMarkupUpdate(event, render.getMessageId(), messageType);
            case DELETE -> messageService.deleteMessage(event.getChatId(), render.getMessageId());
            case PIN -> messageService.pinMessage(event.getChatId(), render.getMessageId());
        };
    }

    private void executeAfterActions(final TEvent event, final Render render) {
        render.getAfterActions().forEach(
            afterAction -> {
                var afterRender = afterAction.get();
                executeMethod(event, afterRender);
            }
        );
    }

    public <T extends Serializable, M extends BotApiMethod<T>> void executeTimedMarkup(
        final M method,
        final Render render
    ) {
        if (method instanceof SendMessage sendMessage) {
            sendMessageWithTimedKeyboard(sendMessage, render);
        }
        if (method instanceof EditMessageReplyMarkup editMarkup) {
            editReplyMarkupTimedKeyboard(editMarkup, render);
        }
        if (method instanceof EditMessageText editMessage) {
            editMessageWithTimedKeyboard(editMessage, render);
        }
    }

    private void sendMessageWithTimedKeyboard(
        final SendMessage sendMessage,
        final Render render
    ) {
        var disabledKeyboard = keyboardService.getWaitingKeyboard();
        sendMessage.setReplyMarkup(disabledKeyboard);
        var message = Optional.of(executeService.execute(sendMessage, render))
            .orElseThrow(() -> new TelegramApiException(ExceptionCause.EXTRACT_RESPONSE_MESSAGE));
        var editMarkup = messageService.buildReplyMarkupUpdate(
            message.getChatId(),
            render.getMessageId(),
            keyboardService.getCheckingKeyboard()
        );

        scheduleMarkupUpdate(
            editMarkup,
            Render.forMarkup(render.getMessageType(), render.getMessageId()).withDelay(Constants.DISABLED_BUTTON_TIMEOUT)
        );
    }

    private void editMessageWithTimedKeyboard(
        final EditMessageText editMessage,
        final Render render
    ) {
        var disabledKeyboard = keyboardService.getWaitingKeyboard();
        editMessage.setReplyMarkup(disabledKeyboard);
        executeService.execute(editMessage, render);

        var editMarkup = messageService.buildReplyMarkupUpdate(
            Long.valueOf(editMessage.getChatId()),
            editMessage.getMessageId(),
            keyboardService.getCheckingKeyboard()
        );

        scheduleMarkupUpdate(
            editMarkup,
            Render.forMarkup(render.getMessageType(), render.getMessageId()).withDelay(Constants.DISABLED_BUTTON_TIMEOUT)
        );
    }

    private void editReplyMarkupTimedKeyboard(
        final EditMessageReplyMarkup editMarkup,
        final Render render
    ) {
        var disabledKeyboard = keyboardService.getWaitingKeyboard();
        editMarkup.setReplyMarkup(disabledKeyboard);
        executeService.execute(editMarkup, render);
        editMarkup.setReplyMarkup(keyboardService.getCheckingKeyboard());

        var event = eventContext.findEvent(Long.valueOf(editMarkup.getChatId()));
        Optional.ofNullable(executeService.executeLater(editMarkup, render))
            .ifPresent(future -> event.getFutures().add(future));
    }

    private void scheduleMarkupUpdate(
        final EditMessageReplyMarkup editMarkup,
        final Render render
    ) {
        // TODO: chat context
        var event = eventContext.findEvent(Long.valueOf(editMarkup.getChatId()));
        Optional.ofNullable(executeService.executeLater(editMarkup, render))
            .ifPresent(future -> event.getFutures().add(future));
    }
}
