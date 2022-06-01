package ru.acuma.shuffler.service.impl;

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
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.KeyboardService;
import ru.acuma.shuffler.service.MessageService;
import ru.acuma.shuffler.util.BuildMessageUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final KeyboardService keyboardService;

    @Override
    public BotApiMethod<Message> sendMessage(GameEvent event, MessageType type) {
        var message = SendMessage.builder()
                .chatId(String.valueOf(event.getChatId()))
                .text(BuildMessageUtil.buildText(event, type))
                .replyMarkup(getKeyboard(event, type))
                .parseMode(ParseMode.HTML)
                .build();
        return message;
    }

    @Override
    public EditMessageText updateMessage(GameEvent event, Integer messageId, MessageType type) {
        var message = EditMessageText.builder()
                .chatId(String.valueOf(event.getChatId()))
                .messageId(messageId)
                .text(BuildMessageUtil.buildText(event, type))
                .replyMarkup(getKeyboard(event, type))
                .parseMode(ParseMode.HTML)
                .build();
        return message;
    }

    @Override
    public EditMessageReplyMarkup updateMarkup(GameEvent event, Integer messageId, MessageType type) {
        var message = EditMessageReplyMarkup.builder()
                .chatId(String.valueOf(event.getChatId()))
                .messageId(messageId)
                .replyMarkup(getKeyboard(event, type))
                .build();
        return message;
    }

    @Override
    public EditMessageText updateLobbyMessage(GameEvent event) {
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

    private InlineKeyboardMarkup getKeyboard(GameEvent event, MessageType type) {
        var state = event.getEventState();
        if (type == MessageType.LOBBY && (state == EventState.BEGIN_CHECKING || state == EventState.CANCEL_LOBBY_CHECKING)) {
            return keyboardService.getEmptyKeyboard();
        }
        return type == MessageType.CHECKING_TIMED
                ? keyboardService.getTimedKeyboard(Values.TIMEOUT)
                : keyboardService.getKeyboard(event, type);
    }
}
