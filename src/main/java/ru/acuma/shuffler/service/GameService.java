package ru.acuma.shuffler.service;

import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgGame;
import ru.acuma.shuffler.model.enums.WinnerState;

public interface GameService {

    TgGame buildGame(TgEvent event);

    void endGame(TgEvent event, WinnerState state);
}
