package ru.acuma.shuffler.service;

import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.entity.Game;
import ru.acuma.shuffler.model.enums.WinnerState;

public interface GameService {

    Game buildGame(GameEvent event);

    void endGame(GameEvent event, WinnerState state);
}
