package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.domain.KickerEvent;

public interface EventContextService {

    KickerEvent buildEvent(Long chatId);

    boolean isActive(Long chatId);

    KickerEvent getEvent(Long chatId);

    void evictEvent(Long chatId);


}
