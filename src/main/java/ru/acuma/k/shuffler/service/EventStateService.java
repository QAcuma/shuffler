package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerEvent;

public interface EventStateService {

    void lobbyState(KickerEvent event);

    void createdState(KickerEvent event);

    void readyState(KickerEvent event);

    void cancelCheckState(KickerEvent event);

    void cancelledState(KickerEvent event);

    void beginCheckState(KickerEvent event);

    void playingState(KickerEvent event);

    void waitingState(KickerEvent event);

    void nextCheckingState(KickerEvent event);

    void redCheckingState(KickerEvent event);

    void blueCheckingState(KickerEvent event);

    void finishCheckState(KickerEvent event);

    void finishedState(KickerEvent event);

}
