package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.acuma.shuffler.context.Render;
import ru.acuma.shuffler.context.RenderContext;
import ru.acuma.shuffler.exception.TelegramApiException;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.constant.messages.MessageAction;
import ru.acuma.shuffler.service.telegram.ExecuteService;

import java.io.Serializable;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RenderService {

    private final RenderContext renderContext;
    private final TelegramMethodService telegramMethodService;
    private final KeyboardService keyboardService;
    private final ExecuteService executeService;

    public void render(final Long chatId) {
        var chatRender = renderContext.forChat(chatId);
        chatRender.getRenders().values()
            .stream()
            .filter(Render::requireUpdate)
            .forEach(render -> {
                var method = resolveApiMethod(render, chatId);
                executeMethod(method, render);
                executeAfterActions(render, chatId);
            });
        chatRender.getRenders().entrySet()
            .removeIf(entry -> entry.getValue().getMessageAction().equals(MessageAction.DELETE));
    }

    public void delete(final Render render, final Long chatId) {
        var method = telegramMethodService.deleteMessage(render, chatId);
        executeService.execute(method, render);
    }

    private void executeMethod(
        final BotApiMethod<?> method,
        final Render render
    ) {
        switch (render.getExecuteStrategy()) {
            case REGULAR -> executeService.execute(method, render);
            case DELAYED -> executeService.executeLater(method, render);
            case TIMER -> executeTimedMarkup(method, render);
            case IDLE -> render.success();
        }
    }

    private BotApiMethod<?> resolveApiMethod(
        final Render render,
        final Long chatId
    ) {
        return switch (render.getMessageAction()) {
            case SEND -> telegramMethodService.sendMessage(render, chatId);
            case UPDATE -> telegramMethodService.buildMessageUpdate(render, chatId);
            case UPDATE_MARKUP -> telegramMethodService.buildReplyMarkupUpdate(render, chatId);
            case DELETE -> telegramMethodService.deleteMessage(render, chatId);
            case PIN -> telegramMethodService.pinMessage(render, chatId);
        };
    }

    private void executeAfterActions(final Render render, final Long chatId) {
        render.getAfterActions().forEach(
            afterAction -> {
                var afterRender = afterAction.get();
                var method = resolveApiMethod(afterRender, chatId);
                executeMethod(method, afterRender);
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

    private void editMessageWithTimedKeyboard(
        final EditMessageText editMessage,
        final Render render
    ) {
        var disabledKeyboard = keyboardService.getWaitingKeyboard();
        editMessage.setReplyMarkup(disabledKeyboard);
        executeService.execute(editMessage, render);

        var editMarkup = telegramMethodService.buildReplyMarkupUpdate(
            Long.valueOf(editMessage.getChatId()),
            editMessage.getMessageId(),
            keyboardService.getCheckingKeyboard()
        );

        scheduleMarkupUpdate(
            editMarkup,
            Render.forMarkup(render.getMessageType()).withDelay(Constants.DISABLED_BUTTON_TIMEOUT)
        );
    }

    private void sendMessageWithTimedKeyboard(
        final SendMessage sendMessage,
        final Render render
    ) {
        var disabledKeyboard = keyboardService.getWaitingKeyboard();
        sendMessage.setReplyMarkup(disabledKeyboard);
        var message = Optional.of(executeService.execute(sendMessage, render))
            .orElseThrow(() -> new TelegramApiException(ExceptionCause.EXTRACT_RESPONSE_MESSAGE));
        var editMarkup = telegramMethodService.buildReplyMarkupUpdate(
            message.getChatId(),
            render.getMessageId(),
            keyboardService.getCheckingKeyboard()
        );

        scheduleMarkupUpdate(
            editMarkup,
            Render.forMarkup(render.getMessageType()).withDelay(Constants.DISABLED_BUTTON_TIMEOUT)
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

        var chatRender = renderContext.forChat(Long.valueOf(editMarkup.getChatId()));
        Optional.ofNullable(executeService.executeLater(editMarkup, render))
            .ifPresent(future -> chatRender.getFutures().add(future));
    }

    private void scheduleMarkupUpdate(
        final EditMessageReplyMarkup editMarkup,
        final Render render
    ) {
        var chatRender = renderContext.forChat(Long.valueOf(editMarkup.getChatId()));
        Optional.ofNullable(executeService.executeLater(editMarkup, render))
            .ifPresent(chatRender::schedule);
    }
}
