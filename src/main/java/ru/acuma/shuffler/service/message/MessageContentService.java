package ru.acuma.shuffler.service.message;

import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;

public interface MessageContentService {

    String buildContent(TgEvent event, MessageType type);
}
