package ru.acuma.shuffler.service;

import ru.acuma.shuffler.model.entity.GameEvent;

public interface EventStateService {

    void lobbyState(GameEvent event);

    void createdState(GameEvent event);

    void readyState(GameEvent event);

    void cancelCheckState(GameEvent event);

    void cancelledState(GameEvent event);

    void beginCheckState(GameEvent event);

    void playingState(GameEvent event);

    void waitingState(GameEvent event);

    void nextCheckingState(GameEvent event);

    void redCheckingState(GameEvent event);

    void blueCheckingState(GameEvent event);

    void finishCheckState(GameEvent event);

    void finishedState(GameEvent event);

}
