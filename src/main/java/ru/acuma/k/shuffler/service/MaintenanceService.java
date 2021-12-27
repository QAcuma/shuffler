package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.domain.KickerEvent;

import java.util.Set;

public interface MaintenanceService {

    void sweepChat(AbsSender absSender, Set<Integer> messages, Long groupId);

    void sweepFromArgs(AbsSender absSender, String[] args, Long groupId);

    void sweepEvent(KickerEvent event, boolean store);

}
