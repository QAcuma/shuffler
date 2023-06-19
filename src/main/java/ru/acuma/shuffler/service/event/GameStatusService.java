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

    public void cancelCheck(TGame game) {
        game.setStatus(GameStatus.CANCEL_CHECKING);
    }

    public void evictCheck(TGame game) {
        game.setStatus(GameStatus.EVICT_CHECKING);
    }

    public void eventCheck(TGame game) {
        game.setStatus(GameStatus.EVENT_CHECKING);
    }

    public void redCheck(TGame game) {
        game.setStatus(GameStatus.RED_CHECKING);
    }

    public void blueCheck(TGame game) {
        game.setStatus(GameStatus.BLUE_CHECKING);
    }

    public void finished(TGame game) {
        game.setStatus(GameStatus.FINISHED)
            .setFinishedAt(TimeMachine.localDateTimeNow());
    }
}
