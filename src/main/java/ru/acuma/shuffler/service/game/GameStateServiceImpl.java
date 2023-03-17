package ru.acuma.shuffler.service.game;

import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.domain.TgGame;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.service.api.GameStateService;

@Service
public class GameStateServiceImpl implements GameStateService {

    @Override
    public void active(TgGame game) {
        game.setState(GameState.ACTIVE);
    }

    @Override
    public void cancel(TgGame game) {
        game.setState(GameState.CANCELLED);
    }

    @Override
    public void cancelCheck(TgGame game) {
        game.setState(GameState.CANCEL_CHECKING);
    }

    @Override
    public void redCheck(TgGame game) {
        game.setState(GameState.RED_CHECKING);
    }

    @Override
    public void blueCheck(TgGame game) {
        game.setState(GameState.BLUE_CHECKING);
    }

    @Override
    public void finished(TgGame game) {
        game.setState(GameState.FINISHED);
    }

    @Override
    public void notExist(TgGame game) {
        game.setState(GameState.NOT_EXIST);
    }
}
