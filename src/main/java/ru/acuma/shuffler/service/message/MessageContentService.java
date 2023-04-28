package ru.acuma.shuffler.service.message;

import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.constant.messages.MessageType;

public interface MessageContentService {

    String buildContent(TEvent event, MessageType type);
}
