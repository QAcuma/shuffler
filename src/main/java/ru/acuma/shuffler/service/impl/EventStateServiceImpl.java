package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.service.EventStateService;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.model.enums.Values;

@Service
@RequiredArgsConstructor
public class EventStateServiceImpl implements EventStateService {

    @Override
    public void lobbyState(GameEvent event) {
        var state = event.getEventState();
        if (state == EventState.CREATED || state == EventState.READY || state == EventState.CANCEL_LOBBY_CHECKING || state == EventState.BEGIN_CHECKING) {
            if (event.getPlayers().size() >= Values.GAME_PLAYERS_COUNT) {
                readyState(event);
            } else {
                createdState(event);
            }
        }
    }

    @Override
    public void createdState(GameEvent event) {
        event.setEventState(EventState.CREATED);
    }

    @Override
    public void readyState(GameEvent event) {
        event.setEventState(EventState.READY);
    }

    @Override
    public void cancelCheckState(GameEvent event) {
        event.setEventState(EventState.CANCEL_LOBBY_CHECKING);
    }

    @Override
    public void cancelledState(GameEvent event) {
        event.setEventState(EventState.CANCELLED);
    }

    @Override
    public void beginCheckState(GameEvent event) {
        event.setEventState(EventState.BEGIN_CHECKING);
    }

    @Override
    public void playingState(GameEvent event) {
        event.setEventState(EventState.PLAYING);
    }

    @Override
    public void waitingState(GameEvent event) {
        event.setEventState(EventState.WAITING);
    }

    @Override
    public void nextCheckingState(GameEvent event) {
        event.getCurrentGame().setState(GameState.CHECKING);
        event.setEventState(EventState.CANCEL_GAME_CHECKING);
    }

    @Override
    public void redCheckingState(GameEvent event) {
        event.getCurrentGame().setState(GameState.CHECKING);
        event.setEventState(EventState.RED_CHECKING);
    }

    @Override
    public void blueCheckingState(GameEvent event) {
        event.getCurrentGame().setState(GameState.CHECKING);
        event.setEventState(EventState.BLUE_CHECKING);
    }

    @Override
    public void finishCheckState(GameEvent event) {
        event.getCurrentGame().setState(GameState.CHECKING);
        event.setEventState(EventState.FINISH_CHECKING);
    }

    @Override
    public void finishedState(GameEvent event) {
        event.setEventState(EventState.FINISHED);
    }
}
