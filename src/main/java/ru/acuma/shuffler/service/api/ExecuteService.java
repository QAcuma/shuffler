package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.shuffler.model.entity.TgEvent;

import java.io.Serializable;

public interface ExecuteService {

    <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method);

    void executeLater(Runnable method, int delay) throws TelegramApiException;

    void executeAsyncTimer(TgEvent event, BotApiMethod<Message> message);

}
