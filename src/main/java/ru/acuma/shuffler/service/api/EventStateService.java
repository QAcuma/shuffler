package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.domain.TEvent;

public interface EventStateService {

    void prepare(TEvent event);

    void active(TEvent event);

    void cancel(TEvent event);

    void gameCheck(TEvent event);

    void cancelled(TEvent event);

    void begin(TEvent event);

    void evicting(TEvent event);

    void finishCheck(TEvent event);

    void finished(TEvent event);

}
