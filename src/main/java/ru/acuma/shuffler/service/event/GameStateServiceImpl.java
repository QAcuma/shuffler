package ru.acuma.shuffler.service.event;

import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.constant.GameState;
import ru.acuma.shuffler.service.api.GameStateService;

@Service
public class GameStateServiceImpl implements GameStateService {

    @Override
    public void active(TGame game) {
        game.setStatus(GameState.ACTIVE);
    }

    @Override
    public void cancel(TGame game) {
        game.setStatus(GameState.CANCELLED);
    }

    @Override
    public void cancelCheck(TGame game) {
        game.setStatus(GameState.CANCEL_CHECKING);
    }

    @Override
    public void redCheck(TGame game) {
        game.setStatus(GameState.RED_CHECKING);
    }

    @Override
    public void blueCheck(TGame game) {
        game.setStatus(GameState.BLUE_CHECKING);
    }

    @Override
    public void finished(TGame game) {
        game.setStatus(GameState.FINISHED);
    }

    @Override
    public void notExist(TGame game) {
        game.setStatus(GameState.NOT_EXIST);
    }
}
