package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.model.enums.Values;
import ru.acuma.k.shuffler.model.enums.messages.MessageType;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.service.MessageService;
import ru.acuma.k.shuffler.util.BuildMessageUtil;

import static ru.acuma.k.shuffler.model.enums.messages.MessageType.CHECKING_TIMED;
import static ru.acuma.k.shuffler.model.enums.messages.MessageType.LOBBY;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final KeyboardService keyboardService;

    @Override
    public BotApiMethod<Message> sendMessage(KickerEvent event, MessageType type) {
        var message = SendMessage.builder()
                .chatId(String.valueOf(event.getChatId()))
                .text(BuildMessageUtil.buildText(event, type))
                .replyMarkup(getKeyboard(event, type))
                .build();
        message.enableMarkdown(true);
        return message;
    }

    @Override
    public EditMessageText updateMessage(KickerEvent event, Integer messageId, MessageType type) {
        var message = EditMessageText.builder()
                .chatId(String.valueOf(event.getChatId()))
                .messageId(messageId)
                .text(BuildMessageUtil.buildText(event, type))
                .replyMarkup(getKeyboard(event, type))
                .build();
        return message;
    }

    @Override
    public EditMessageReplyMarkup updateMarkup(KickerEvent event, Integer messageId, MessageType type) {
        var message = EditMessageReplyMarkup.builder()
                .chatId(String.valueOf(event.getChatId()))
                .messageId(messageId)
                .replyMarkup(getKeyboard(event, type))
                .build();
        return message;
    }

    @Override
    public EditMessageText updateLobbyMessage(KickerEvent event) {
        return updateMessage(event, event.getBaseMessage(), LOBBY);
    }

    @Override
    public DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .build();
    }

    private InlineKeyboardMarkup getKeyboard(KickerEvent event, MessageType type) {
        var state = event.getEventState();
        if (type == LOBBY && (state == EventState.BEGIN_CHECKING || state == EventState.CANCEL_LOBBY_CHECKING)) {
            return keyboardService.getEmptyKeyboard();
        }
        return type == CHECKING_TIMED
                ? keyboardService.getTimedKeyboard(Values.TIMEOUT)
                : keyboardService.getKeyboard(event, type);
    }
}
