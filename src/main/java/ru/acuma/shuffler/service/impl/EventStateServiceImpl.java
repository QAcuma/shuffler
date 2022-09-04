package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.service.api.EventStateService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventStateServiceImpl implements EventStateService {

    @Override
    public void definePreparingState(TgEvent event) {
        var state = event.getEventState();
        if (isPreparingState(state)) {
            if (event.getPlayers().size() >= Values.GAME_PLAYERS_COUNT) {
                readyState(event);

                return;
            }
            createdState(event);
        }
    }

    @Override
    public void defineActiveState(TgEvent event) {
        if (event.getActivePlayers().size() >= Values.GAME_PLAYERS_COUNT) {
            playingState(event);

            return;
        }
        if (event.getLatestGame().getState().in(GameState.ACTIVE, GameState.CANCEL_CHECKING, GameState.RED_CHECKING, GameState.BLUE_CHECKING)) {
            waitingWithGameState(event);

            return;
        }
        waitingState(event);
    }

    private boolean isPreparingState(EventState state) {
        return state.in(EventState.CREATED, EventState.READY, EventState.CANCEL_CHECKING, EventState.BEGIN_CHECKING);
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
        event.setEventState(EventState.CANCEL_CHECKING);
    }

    @Override
    public void cancelledState(TgEvent event) {
        event.setEventState(EventState.CANCELLED);
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
    public void waitingWithGameState(TgEvent event) {
        event.setEventState(EventState.WAITING_WITH_GAME);
    }

    @Override
    public void finishCheckState(TgEvent event) {
        event.setEventState(EventState.FINISH_CHECKING);
    }

    @Override
    public void finishedState(TgEvent event) {
        event.setEventState(EventState.FINISHED);
        event.setFinishedAt(LocalDateTime.now());
    }
}
