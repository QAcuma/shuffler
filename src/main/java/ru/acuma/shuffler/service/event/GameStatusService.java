package ru.acuma.shuffler.service.event;

import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.constant.GameStatus;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.util.TimeMachine;

@Service
public class GameStatusService {

    public void active(TGame game) {
        game.setStatus(GameStatus.ACTIVE);
    }

    public void cancelled(TGame game) {
        game.setStatus(GameStatus.CANCELLED)
            .setFinishedAt(TimeMachine.localDateTimeNow());
    }

    public void cancelChecking(TGame game) {
        game.setStatus(GameStatus.CANCEL_CHECKING);
    }

    public void evictChecking(TGame game) {
        game.setStatus(GameStatus.EVICT_CHECKING);
    }

    public void eventChecking(TGame game) {
        game.setStatus(GameStatus.EVENT_CHECKING);
    }

    public void redChecking(TGame game) {
        game.setStatus(GameStatus.RED_CHECKING);
    }

    public void blueChecking(TGame game) {
        game.setStatus(GameStatus.BLUE_CHECKING);
    }

    public void finished(TGame game) {
        game.setStatus(GameStatus.FINISHED)
            .setFinishedAt(TimeMachine.localDateTimeNow());
    }
}
