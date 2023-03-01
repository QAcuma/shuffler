package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.dto.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;

public interface MessageService {

    BotApiMethod<Message> buildMessage(TgEvent event, MessageType type);

    EditMessageText buildMessageUpdate(TgEvent event, Integer messageId, MessageType type);

    EditMessageReplyMarkup buildReplyMarkupUpdate(TgEvent event, Integer messageId, MessageType type);

    EditMessageText buildLobbyMessageUpdate(TgEvent event);

    PinChatMessage pinMessage(Message message);

    DeleteMessage deleteMessage(Long chatId, Integer messageId);

}
