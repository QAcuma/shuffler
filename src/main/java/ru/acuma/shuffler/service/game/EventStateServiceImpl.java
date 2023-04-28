package ru.acuma.shuffler.service.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.GameState;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.service.api.EventStateService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventStateServiceImpl implements EventStateService {

    @Override
    public void prepare(TEvent event) {
        var state = event.getEventStatus();
        if (isPreparingState(state)) {
            if (event.getPlayers().size() >= Constants.GAME_PLAYERS_COUNT) {
                ready(event);

                return;
            }
            created(event);
        }
    }

    @Override
    public void active(TEvent event) {
        if (event.getActivePlayers().size() >= Constants.GAME_PLAYERS_COUNT) {
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
    public void cancel(TEvent event) {
        event.setEventStatus(EventStatus.CANCEL_CHECKING);
    }

    @Override
    public void gameCheck(TEvent event) {
        event.setEventStatus(EventStatus.GAME_CHECKING);
    }

    @Override
    public void cancelled(TEvent event) {
        event.setEventStatus(EventStatus.CANCELLED);
    }

    @Override
    public void begin(TEvent event) {
        event.setEventStatus(EventStatus.BEGIN_CHECKING);
    }

    @Override
    public void evicting(TEvent event) {
        event.setEventStatus(EventStatus.EVICTING);
    }

    @Override
    public void finishCheck(TEvent event) {
        event.setEventStatus(EventStatus.FINISH_CHECKING);
    }

    @Override
    public void finished(TEvent event) {
        event.setEventStatus(EventStatus.FINISHED);
        event.setFinishedAt(LocalDateTime.now());
    }

    private boolean isPreparingState(EventStatus state) {
        return state.in(EventStatus.CREATED, EventStatus.READY, EventStatus.CANCEL_CHECKING, EventStatus.BEGIN_CHECKING);
    }

    private void created(TEvent event) {
        event.setEventStatus(EventStatus.CREATED);
    }

    private void ready(TEvent event) {
        event.setEventStatus(EventStatus.READY);
    }

    private void waiting(TEvent event) {
        event.setEventStatus(EventStatus.WAITING);
    }

    private void playing(TEvent event) {
        event.setEventStatus(EventStatus.PLAYING);
    }

    private void waitingWithGame(TEvent event) {
        event.setEventStatus(EventStatus.WAITING_WITH_GAME);
    }
}
