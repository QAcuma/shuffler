package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.dto.TgGame;

public interface GameStateService {

    void active(TgGame game);

    void cancel(TgGame game);

    void cancelCheck(TgGame game);

    void redCheck(TgGame game);

    void blueCheck(TgGame game);

    void finished(TgGame game);

    void notExist(TgGame game);

}
