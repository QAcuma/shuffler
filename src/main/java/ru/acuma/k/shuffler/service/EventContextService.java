package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.shufflerlib.model.Discipline;

public interface EventContextService {

    KickerEvent buildEvent(Long chatId, Discipline discipline);

    boolean isActive(Long chatId);

    KickerEvent getCurrentEvent(Long chatId);

    void evictEvent(Long chatId);


}
