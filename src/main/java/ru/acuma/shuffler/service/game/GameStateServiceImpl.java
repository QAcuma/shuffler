package ru.acuma.shuffler.service.game;

import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.constant.GameState;
import ru.acuma.shuffler.service.api.GameStateService;

@Service
public class GameStateServiceImpl implements GameStateService {

    @Override
    public void active(TGame game) {
        game.setState(GameState.ACTIVE);
    }

    @Override
    public void cancel(TGame game) {
        game.setState(GameState.CANCELLED);
    }

    @Override
    public void cancelCheck(TGame game) {
        game.setState(GameState.CANCEL_CHECKING);
    }

    @Override
    public void redCheck(TGame game) {
        game.setState(GameState.RED_CHECKING);
    }

    @Override
    public void blueCheck(TGame game) {
        game.setState(GameState.BLUE_CHECKING);
    }

    @Override
    public void finished(TGame game) {
        game.setState(GameState.FINISHED);
    }

    @Override
    public void notExist(TGame game) {
        game.setState(GameState.NOT_EXIST);
    }
}
