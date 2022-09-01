package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import ru.acuma.shuffler.bot.ShufflerBot;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.KeyboardService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.util.BuildMessageUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final KeyboardService keyboardService;

    private ShufflerBot shufflerBot;

    @Autowired
    public void setShufflerBot(@Lazy ShufflerBot shufflerBot) {
        this.shufflerBot = shufflerBot;
    }

    @Override
    public BotApiMethod<Message> sendMessage(TgEvent event, MessageType type) {
        return SendMessage.builder()
                .chatId(String.valueOf(event.getChatId()))
                .text(BuildMessageUtil.buildText(event, type))
                .replyMarkup(getKeyboard(event, type))
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public void sendMessage(SendMessage message) {
        shufflerBot.reply(message);
    }

    @Override
    public EditMessageText updateMessage(TgEvent event, Integer messageId, MessageType type) {
        return EditMessageText.builder()
                .chatId(String.valueOf(event.getChatId()))
                .messageId(messageId)
                .text(BuildMessageUtil.buildText(event, type))
                .replyMarkup(getKeyboard(event, type))
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public EditMessageReplyMarkup updateMarkup(TgEvent event, Integer messageId, MessageType type) {
        return EditMessageReplyMarkup.builder()
                .chatId(String.valueOf(event.getChatId()))
                .messageId(messageId)
                .replyMarkup(getKeyboard(event, type))
                .build();
    }

    @Override
    public EditMessageText updateLobbyMessage(TgEvent event) {
        return updateMessage(event, event.getBaseMessage(), MessageType.LOBBY);
    }

    @Override
    public PinChatMessage pinedMessage(Message message) {
        return PinChatMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .messageId(message.getMessageId())
                .build();
    }

    @Override
    public DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .build();
    }

    private InlineKeyboardMarkup getKeyboard(TgEvent event, MessageType type) {
        var state = event.getEventState();
        if (type == MessageType.LOBBY && (state == EventState.BEGIN_CHECKING || state == EventState.CANCEL_LOBBY_CHECKING)) {
            return keyboardService.getEmptyKeyboard();
        }
        return type == MessageType.CHECKING_TIMED
                ? keyboardService.getTimedKeyboard(Values.TIMEOUT)
                : keyboardService.getKeyboard(event, type);
    }
}
