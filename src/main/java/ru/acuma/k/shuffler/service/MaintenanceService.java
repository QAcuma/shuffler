package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.entity.KickerEvent;

public interface MaintenanceService {

    void sweepChat(AbsSender absSender, KickerEvent event);

    void sweepMessage(AbsSender absSender, Message message);

    void sweepMessage(AbsSender absSender, Long chatId, Integer messageId);

    void sweepEvent(KickerEvent event, boolean store);

}
