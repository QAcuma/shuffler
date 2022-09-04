package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.entity.TgGame;

public interface GameStateService {

    void activeState(TgGame event);

    void cancelState(TgGame event);

    void cancelCheckingState(TgGame game);

    void redCheckingState(TgGame event);

    void blueCheckingState(TgGame event);

    void finishedState(TgGame event);

}
