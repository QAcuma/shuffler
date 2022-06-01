package ru.acuma.shuffler.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;

public interface MessageService {

    BotApiMethod<Message> sendMessage(GameEvent event, MessageType type);

    EditMessageText updateMessage(GameEvent event, Integer messageId, MessageType type);

    EditMessageReplyMarkup updateMarkup(GameEvent event, Integer messageId, MessageType type);

    EditMessageText updateLobbyMessage(GameEvent event);

    PinChatMessage pinedMessage(Message message);

    DeleteMessage deleteMessage(Long chatId, Integer messageId);

}
