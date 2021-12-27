package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.domain.KickerEvent;

public interface DelayedService {

    int getTimeout();

    void processCheckingTimer(AbsSender absSender, SendMessage message, KickerEvent event);

}
