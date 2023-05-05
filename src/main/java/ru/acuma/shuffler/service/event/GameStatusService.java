package ru.acuma.shuffler.service.event;

import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.constant.GameStatus;
import ru.acuma.shuffler.model.domain.TGame;

@Service
public class GameStatusService {

    public void active(TGame game) {
        game.setStatus(GameStatus.ACTIVE);
    }

    public void cancel(TGame game) {
        game.setStatus(GameStatus.CANCELLED);
    }

    public void cancelCheck(TGame game) {
        game.setStatus(GameStatus.CANCEL_CHECKING);
    }

    public void redCheck(TGame game) {
        game.setStatus(GameStatus.RED_CHECKING);
    }

    public void blueCheck(TGame game) {
        game.setStatus(GameStatus.BLUE_CHECKING);
    }

    public void finished(TGame game) {
        game.setStatus(GameStatus.FINISHED);
    }
}
