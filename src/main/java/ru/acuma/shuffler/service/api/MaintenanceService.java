package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.dto.TgEvent;

public interface MaintenanceService {

    void sweepChat(TgEvent event);

    void sweepMessage(Message message);

    void sweepMessage(Long chatId, Integer messageId);

    void sweepEvent(TgEvent event);

}
