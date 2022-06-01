package ru.acuma.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.model.entity.GameEvent;

public interface MaintenanceService {

    void sweepChat(AbsSender absSender, GameEvent event);

    void sweepMessage(AbsSender absSender, Message message);

    void sweepMessage(AbsSender absSender, Long chatId, Integer messageId);

    void sweepEvent(GameEvent event, boolean store);

}
