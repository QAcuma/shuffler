package ru.acuma.shuffler.service;

import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shufflerlib.model.Discipline;

public interface EventContextService {

    TgEvent buildEvent(Long chatId, Discipline discipline);

    boolean isActive(Long chatId);

    TgEvent getCurrentEvent(Long chatId);

    Discipline getCurrentDiscipline(Long chatId);

    void evictEvent(Long chatId);

    TgEvent update(TgEvent event);
}
