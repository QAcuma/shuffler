package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.model.enums.GameState;
import ru.acuma.k.shuffler.service.EventStateService;

import static ru.acuma.k.shuffler.model.enums.EventState.BEGIN_CHECKING;
import static ru.acuma.k.shuffler.model.enums.EventState.BLUE_CHECKING;
import static ru.acuma.k.shuffler.model.enums.EventState.CANCELLED;
import static ru.acuma.k.shuffler.model.enums.EventState.CANCEL_LOBBY_CHECKING;
import static ru.acuma.k.shuffler.model.enums.EventState.CREATED;
import static ru.acuma.k.shuffler.model.enums.EventState.READY;
import static ru.acuma.k.shuffler.model.enums.EventState.RED_CHECKING;
import static ru.acuma.k.shuffler.model.enums.GameState.CHECKING;
import static ru.acuma.k.shuffler.model.enums.GameState.STARTED;
import static ru.acuma.k.shuffler.model.enums.Values.GAME_PLAYERS_COUNT;

@Service
@RequiredArgsConstructor
public class EventStateServiceImpl implements EventStateService {

    @Override
    public void lobbyState(KickerEvent event) {
        var state = event.getEventState();
        if (state == CREATED || state == READY || state == CANCEL_LOBBY_CHECKING || state == BEGIN_CHECKING) {
            if (event.getPlayers().size() >= GAME_PLAYERS_COUNT) {
                readyState(event);
            } else {
                createdState(event);
            }
        }
    }

    @Override
    public void createdState(KickerEvent event) {
        event.setEventState(CREATED);
    }

    @Override
    public void readyState(KickerEvent event) {
        event.setEventState(READY);
    }

    @Override
    public void cancelCheckState(KickerEvent event) {
        event.setEventState(CANCEL_LOBBY_CHECKING);
    }

    @Override
    public void cancelledState(KickerEvent event) {
        event.setEventState(CANCELLED);
    }

    @Override
    public void beginCheckState(KickerEvent event) {
        event.setEventState(BEGIN_CHECKING);
    }

    @Override
    public void playingState(KickerEvent event) {
        event.setEventState(EventState.PLAYING);
    }

    @Override
    public void nextCheckingState(KickerEvent event) {
        event.getCurrentGame().setState(CHECKING);
        event.setEventState(EventState.CANCEL_GAME_CHECKING);
    }

    @Override
    public void redCheckingState(KickerEvent event) {
        event.getCurrentGame().setState(CHECKING);
        event.setEventState(RED_CHECKING);
    }

    @Override
    public void blueCheckingState(KickerEvent event) {
        event.getCurrentGame().setState(CHECKING);
        event.setEventState(BLUE_CHECKING);
    }

    @Override
    public void finishCheckState(KickerEvent event) {
        event.getCurrentGame().setState(CHECKING);
        event.setEventState(EventState.FINISH_CHECKING);
    }

    @Override
    public void finishedState(KickerEvent event) {
        event.setEventState(EventState.FINISHED);
    }
}
