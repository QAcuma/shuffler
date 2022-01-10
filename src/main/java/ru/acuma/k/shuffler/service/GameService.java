package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.model.enums.WinnerState;

public interface GameService {

    KickerGame buildGame(KickerEvent event);

    void endGame(KickerEvent event, WinnerState state);
}
