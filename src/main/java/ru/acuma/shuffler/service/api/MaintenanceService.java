package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.model.entity.TgEvent;

public interface MaintenanceService {

    void sweepChat(AbsSender absSender, TgEvent event);

    void sweepMessage(AbsSender absSender, Message message);

    void sweepMessage(AbsSender absSender, Long chatId, Integer messageId);

    void sweepEvent(TgEvent event, boolean store);

}
