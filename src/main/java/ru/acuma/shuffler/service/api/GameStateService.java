package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.entity.TgGame;

public interface GameStateService {

    void activeState(TgGame game);

    void cancelState(TgGame game);

    void cancelCheckingState(TgGame game);

    void redCheckingState(TgGame game);

    void blueCheckingState(TgGame game);

    void finishedState(TgGame game);

    void notExistState(TgGame game);

}
