package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.service.EventStateService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventStateServiceImpl implements EventStateService {

    @Override
    public void lobbyState(TgEvent event) {
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
    public void createdState(TgEvent event) {
        event.setEventState(EventState.CREATED);
    }

    @Override
    public void readyState(TgEvent event) {
        event.setEventState(EventState.READY);
    }

    @Override
    public void cancelCheckState(TgEvent event) {
        event.setEventState(EventState.CANCEL_LOBBY_CHECKING);
    }

    @Override
    public void cancelledState(TgEvent event) {
        event.setEventState(EventState.CANCELLED);
        event.setFinishedAt(LocalDateTime.now());
    }

    @Override
    public void beginCheckState(TgEvent event) {
        event.setEventState(EventState.BEGIN_CHECKING);
    }

    @Override
    public void playingState(TgEvent event) {
        event.setEventState(EventState.PLAYING);
    }

    @Override
    public void waitingState(TgEvent event) {
        event.setEventState(EventState.WAITING);
    }

    @Override
    public void cancelGameCheckingState(TgEvent event) {
        event.getCurrentGame().setState(GameState.CHECKING);
        event.setEventState(EventState.CANCEL_GAME_CHECKING);
    }

    @Override
    public void redCheckingState(TgEvent event) {
        event.getCurrentGame().setState(GameState.CHECKING);
        event.setEventState(EventState.RED_CHECKING);
    }

    @Override
    public void blueCheckingState(TgEvent event) {
        event.getCurrentGame().setState(GameState.CHECKING);
        event.setEventState(EventState.BLUE_CHECKING);
    }

    @Override
    public void finishCheckState(TgEvent event) {
        event.getCurrentGame().setState(GameState.CHECKING);
        event.setEventState(EventState.FINISH_CHECKING);
    }

    @Override
    public void finishedState(TgEvent event) {
        event.setEventState(EventState.FINISHED);
        event.setFinishedAt(LocalDateTime.now());
    }
}
