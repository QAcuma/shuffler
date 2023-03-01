package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.dto.TgEvent;

public interface GameService {

    void nextGame(TgEvent event);

    void handleGameCheck(TgEvent event);
}
