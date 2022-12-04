package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.entity.TgEvent;

public interface EventStateService {

    void prepare(TgEvent event);

    void active(TgEvent event);

    void cancel(TgEvent event);

    void gameCheck(TgEvent event);

    void cancelled(TgEvent event);

    void begin(TgEvent event);

    void evicting(TgEvent event);

    void finishCheck(TgEvent event);

    void finished(TgEvent event);

}
