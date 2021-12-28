package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.domain.KickerEvent;

import java.util.Set;

public interface MaintenanceService {

    void sweepChat(AbsSender absSender, Set<Integer> messages, Long chatId);

    void sweepContext(AbsSender absSender, String[] args, Long chatId);

    void sweepEvent(KickerEvent event, boolean store);

}
