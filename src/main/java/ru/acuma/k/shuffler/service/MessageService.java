package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.messages.MessageType;

public interface MessageService {

    BotApiMethod<Message> sendMessage(KickerEvent event, MessageType type);

    EditMessageText updateMessage(KickerEvent event, Integer messageId, MessageType type);

    EditMessageReplyMarkup updateMarkup(KickerEvent event, Integer messageId, MessageType type);

    EditMessageText updateLobbyMessage(KickerEvent event);

    PinChatMessage pinedMessage(Message message);

    DeleteMessage deleteMessage(Long chatId, Integer messageId);

}
