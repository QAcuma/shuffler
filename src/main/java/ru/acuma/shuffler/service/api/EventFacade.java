package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;

import java.util.function.BiConsumer;

public interface EventFacade {
    void finishEventActions(TgEvent event, Message message);

    void checkingStateActions(TgEvent event);

    BiConsumer<Message, TgEvent> getCheckingConsumer();
}
