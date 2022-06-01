package ru.acuma.shuffler.service;

import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shufflerlib.model.Discipline;

public interface EventContextService {

    GameEvent buildEvent(Long chatId, Discipline discipline);

    boolean isActive(Long chatId);

    GameEvent getCurrentEvent(Long chatId);

    void evictEvent(Long chatId);


}
