package ru.acuma.shuffler.service.game;

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
    public void prepare(TgEvent event) {
        var state = event.getEventState();
        if (isPreparingState(state)) {
            if (event.getPlayers().size() >= Values.GAME_PLAYERS_COUNT) {
                ready(event);

                return;
            }
            created(event);
        }
    }

    @Override
    public void active(TgEvent event) {
        if (event.getActivePlayers().size() >= Values.GAME_PLAYERS_COUNT) {
            playing(event);

            return;
        }
        if (event.getLatestGame().getState().in(GameState.ACTIVE, GameState.CANCEL_CHECKING, GameState.RED_CHECKING, GameState.BLUE_CHECKING)) {
            waitingWithGame(event);

            return;
        }
        waiting(event);
    }

    @Override
    public void cancel(TgEvent event) {
        event.setEventState(EventState.CANCEL_CHECKING);
    }

    @Override
    public void gameCheck(TgEvent event) {
        event.setEventState(EventState.GAME_CHECKING);
    }

    @Override
    public void cancelled(TgEvent event) {
        event.setEventState(EventState.CANCELLED);
    }

    @Override
    public void begin(TgEvent event) {
        event.setEventState(EventState.BEGIN_CHECKING);
    }

    @Override
    public void evicting(TgEvent event) {
        event.setEventState(EventState.EVICTING);
    }

    @Override
    public void finishCheck(TgEvent event) {
        event.setEventState(EventState.FINISH_CHECKING);
    }

    @Override
    public void finished(TgEvent event) {
        event.setEventState(EventState.FINISHED);
        event.setFinishedAt(LocalDateTime.now());
    }

    private boolean isPreparingState(EventState state) {
        return state.in(EventState.CREATED, EventState.READY, EventState.CANCEL_CHECKING, EventState.BEGIN_CHECKING);
    }

    private void created(TgEvent event) {
        event.setEventState(EventState.CREATED);
    }

    private void ready(TgEvent event) {
        event.setEventState(EventState.READY);
    }

    private void waiting(TgEvent event) {
        event.setEventState(EventState.WAITING);
    }

    private void playing(TgEvent event) {
        event.setEventState(EventState.PLAYING);
    }

    private void waitingWithGame(TgEvent event) {
        event.setEventState(EventState.WAITING_WITH_GAME);
    }
}
