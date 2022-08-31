package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;

public interface MessageService {

    BotApiMethod<Message> sendMessage(TgEvent event, MessageType type);

    void sendMessage(SendMessage message);

    EditMessageText updateMessage(TgEvent event, Integer messageId, MessageType type);

    EditMessageReplyMarkup updateMarkup(TgEvent event, Integer messageId, MessageType type);

    EditMessageText updateLobbyMessage(TgEvent event);

    PinChatMessage pinedMessage(Message message);

    DeleteMessage deleteMessage(Long chatId, Integer messageId);

}
