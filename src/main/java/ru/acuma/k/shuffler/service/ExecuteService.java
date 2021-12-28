package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.domain.KickerEvent;

import java.io.Serializable;

public interface ExecuteService {

    <T extends Serializable, Method extends BotApiMethod<T>> T execute(AbsSender absSender, Method method);

    void executeAsync(AbsSender absSender, KickerEvent event, BotApiMethod<Message> message);

}
