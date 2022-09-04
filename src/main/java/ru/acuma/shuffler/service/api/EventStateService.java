package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.entity.TgEvent;

public interface EventStateService {

    void definePreparingState(TgEvent event);

    void defineActiveState(TgEvent event);

    void createdState(TgEvent event);

    void cancelCheckState(TgEvent event);

    void cancelledState(TgEvent event);

    void readyState(TgEvent event);

    void beginCheckState(TgEvent event);

    void playingState(TgEvent event);

    void waitingState(TgEvent event);

    void waitingWithGameState(TgEvent event);

    void finishCheckState(TgEvent event);

    void finishedState(TgEvent event);

}
