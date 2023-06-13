package ru.acuma.shuffler.service.message;

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
import ru.acuma.shuffler.context.cotainer.Render;

@Service
public class TelegramMethodService {

    public BotApiMethod<Message> sendMessage(final Render render, final Long chatId) {
        return SendMessage.builder()
            .chatId(chatId)
            .text(render.getMessageText())
            .replyMarkup(render.getKeyboard())
            .parseMode(ParseMode.HTML)
            .build();
    }

    public EditMessageText buildMessageUpdate(final Render render, final Long chatId) {
        return EditMessageText.builder()
            .chatId(chatId)
            .messageId(render.getMessageId())
            .text(render.getMessageText())
            .replyMarkup(render.getKeyboard())
            .parseMode(ParseMode.HTML)
            .build();
    }

    public EditMessageReplyMarkup buildReplyMarkupUpdate(final Render render, final Long chatId) {
        return EditMessageReplyMarkup.builder()
            .chatId(chatId)
            .messageId(render.getMessageId())
            .replyMarkup(render.getKeyboard())
            .build();
    }

    public EditMessageReplyMarkup buildReplyMarkupUpdate(final Long chatId, final Integer messageId, final InlineKeyboardMarkup keyboard) {
        return EditMessageReplyMarkup.builder()
            .chatId(String.valueOf(chatId))
            .messageId(messageId)
            .replyMarkup(keyboard)
            .build();
    }

    public PinChatMessage pinMessage(final Render render, final Long chatId) {
        return PinChatMessage.builder()
            .chatId(chatId)
            .disableNotification(true)
            .messageId(render.getMessageId())
            .build();
    }

    public DeleteMessage deleteMessage(final Render render, final Long chatId) {
        return DeleteMessage.builder()
            .chatId(chatId)
            .messageId(render.getMessageId())
            .build();
    }
}
