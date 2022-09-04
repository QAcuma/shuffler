package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.entity.TgEvent;

public interface GameService {

    void nextGame(TgEvent event);

    void applyGameChecking(TgEvent event);
}
