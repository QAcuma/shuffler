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
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final KeyboardService keyboardService;
    private final MessageContentService messageContentService;

    public BotApiMethod<Message> sendMessage(final TEvent event, final MessageType type) {
        return SendMessage.builder()
            .chatId(String.valueOf(event.getChatId()))
            .text(messageContentService.buildContent(event, type))
            .replyMarkup(getKeyboard(event, type))
            .parseMode(ParseMode.HTML)
            .build();
    }

    public BotApiMethod<Message> sendMessage(final TEvent event, final MessageType type, final InlineKeyboardMarkup keyboard) {
        return SendMessage.builder()
            .chatId(String.valueOf(event.getChatId()))
            .text(messageContentService.buildContent(event, type))
            .replyMarkup(keyboard)
            .parseMode(ParseMode.HTML)
            .build();
    }

    public EditMessageText buildMessageUpdate(final TEvent event, final Integer messageId, final MessageType type) {
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

    public PinChatMessage pinMessage(final Long chatId, final Integer messageId) {
        return PinChatMessage.builder()
            .chatId(String.valueOf(chatId))
            .messageId(messageId)
            .build();
    }

    public DeleteMessage deleteMessage(final Long chatId, final Integer messageId) {
        return DeleteMessage.builder()
            .chatId(String.valueOf(chatId))
            .messageId(messageId)
            .build();
    }

    private InlineKeyboardMarkup getKeyboard(final TEvent event, final MessageType type) {
        return switch (type) {
            case LOBBY -> keyboardService.getLobbyKeyboard(event);
            case CHECKING -> keyboardService.getCheckingKeyboard(0);
            case GAME -> keyboardService.getGamingKeyboard(event);
            case CANCELLED, STAT -> keyboardService.getEmptyKeyboard();
        };
    }
}
