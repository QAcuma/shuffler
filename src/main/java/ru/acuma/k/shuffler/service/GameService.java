package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.WinnerState;

public interface GameService {

    void buildGame(KickerEvent event);

    void finishGame(KickerEvent event, WinnerState state);
}
