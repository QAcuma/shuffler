package ru.acuma.shuffler.service.impl;

import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.entity.TgGame;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.service.api.GameStateService;

@Service
public class GameStateServiceImpl implements GameStateService {

    @Override
    public void activeState(TgGame game) {
        game.setState(GameState.ACTIVE);
    }

    @Override
    public void cancelState(TgGame game) {
        game.setState(GameState.CANCELLED);
    }

    @Override
    public void cancelCheckingState(TgGame game) {
        game.setState(GameState.CANCEL_CHECKING);
    }

    @Override
    public void redCheckingState(TgGame game) {
        game.setState(GameState.RED_CHECKING);
    }

    @Override
    public void blueCheckingState(TgGame game) {
        game.setState(GameState.BLUE_CHECKING);
    }

    @Override
    public void finishedState(TgGame game) {
        game.setState(GameState.FINISHED);
    }

    @Override
    public void notExistState(TgGame game) {
        game.setState(GameState.NOT_EXIST);
    }
}
