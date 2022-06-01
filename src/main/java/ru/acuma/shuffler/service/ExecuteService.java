package ru.acuma.shuffler.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.shuffler.model.entity.GameEvent;

import java.io.Serializable;

public interface ExecuteService {

    <T extends Serializable, Method extends BotApiMethod<T>> T execute(AbsSender absSender, Method method) throws TelegramApiException;

    void executeLater(AbsSender absSender, Runnable method, int delay) throws TelegramApiException;

    void executeAsyncTimer(AbsSender absSender, GameEvent event, BotApiMethod<Message> message);

}
