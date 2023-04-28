package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.domain.TGame;

public interface GameStateService {

    void active(TGame game);

    void cancel(TGame game);

    void cancelCheck(TGame game);

    void redCheck(TGame game);

    void blueCheck(TGame game);

    void finished(TGame game);

    void notExist(TGame game);

}
