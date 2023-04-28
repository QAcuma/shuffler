package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final KeyboardService keyboardService;
    private final MessageContentService messageContentService;

    public BotApiMethod<Message> buildMessage(TEvent event, MessageType type) {
        return SendMessage.builder()
            .chatId(String.valueOf(event.getChatId()))
            .text(messageContentService.buildContent(event, type))
            .replyMarkup(getKeyboard(event, type))
            .parseMode(ParseMode.HTML)
            .build();
    }

    public EditMessageText buildMessageUpdate(TEvent event, Integer messageId, MessageType type) {
        return EditMessageText.builder()
            .chatId(String.valueOf(event.getChatId()))
            .messageId(messageId)
            .text(messageContentService.buildContent(event, type))
            .replyMarkup(getKeyboard(event, type))
            .parseMode(ParseMode.HTML)
            .build();
    }

    public EditMessageReplyMarkup buildReplyMarkupUpdate(final TEvent event, final Integer messageId, MessageType type) {
        return EditMessageReplyMarkup.builder()
            .chatId(String.valueOf(event.getChatId()))
            .messageId(messageId)
            .replyMarkup(getKeyboard(event, type))
            .build();
    }

    public EditMessageReplyMarkup buildReplyMarkupUpdate(final TEvent event, final Integer messageId, final InlineKeyboardMarkup keyboard) {
        return EditMessageReplyMarkup.builder()
            .chatId(String.valueOf(event.getChatId()))
            .messageId(messageId)
            .replyMarkup(keyboard)
            .build();
    }

    public EditMessageText buildLobbyMessageUpdate(TEvent event) {
        return buildMessageUpdate(event, event.getLobbyMessageId(), MessageType.LOBBY);
    }

    public PinChatMessage pinMessage(final Long chatId, final Integer messageId) {
        return PinChatMessage.builder()
            .chatId(String.valueOf(chatId))
            .messageId(messageId)
            .build();
    }

    public DeleteMessage deleteMessage(TEvent event, Integer messageId) {
        return DeleteMessage.builder()
            .chatId(String.valueOf(event.getChatId()))
            .messageId(messageId)
            .build();
    }

    private InlineKeyboardMarkup getKeyboard(TEvent event, MessageType type) {
        return switch (type) {
            case LOBBY -> keyboardService.getLobbyKeyboard(event);
            case CHECKING -> keyboardService.getCheckingKeyboard(event);
            case GAME -> keyboardService.getGamingKeyboard(event);
            case CHECKING_TIMED -> keyboardService.getTimedKeyboard(Constants.DISABLED_BUTTON_TIMEOUT);
            case CANCELLED, STAT -> keyboardService.getEmptyKeyboard();
        };
    }
}
