package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.domain.TEvent;

public interface GameService {

    void nextGame(TEvent event);

    void handleGameCheck(TEvent event);
}
