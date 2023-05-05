package ru.acuma.shuffler.service.event;

import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.constant.GameState;
import ru.acuma.shuffler.model.domain.TGame;

@Service
public class GameStatusService {

    public void active(TGame game) {
        game.setStatus(GameState.ACTIVE);
    }

    public void cancel(TGame game) {
        game.setStatus(GameState.CANCELLED);
    }

    public void cancelCheck(TGame game) {
        game.setStatus(GameState.CANCEL_CHECKING);
    }

    public void redCheck(TGame game) {
        game.setStatus(GameState.RED_CHECKING);
    }

    public void blueCheck(TGame game) {
        game.setStatus(GameState.BLUE_CHECKING);
    }

    public void finished(TGame game) {
        game.setStatus(GameState.FINISHED);
    }
}
