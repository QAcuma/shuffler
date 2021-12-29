package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.service.EventStateService;

import static ru.acuma.k.shuffler.model.enums.EventState.BEGIN_CHECKING;
import static ru.acuma.k.shuffler.model.enums.EventState.CANCELLED;
import static ru.acuma.k.shuffler.model.enums.EventState.CANCEL_CHECKING;
import static ru.acuma.k.shuffler.model.enums.EventState.CREATED;
import static ru.acuma.k.shuffler.model.enums.EventState.READY;
import static ru.acuma.k.shuffler.model.enums.Values.READY_SIZE;

@Service
@RequiredArgsConstructor
public class EventStateServiceImpl implements EventStateService {

    @Override
    public void lobbyState(KickerEvent event) {
        var state = event.getEventState();
        if (state == CREATED || state == READY || state == CANCEL_CHECKING || state == BEGIN_CHECKING) {
            if (event.getPlayers().size() >= READY_SIZE) {
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
        event.setEventState(CANCEL_CHECKING);
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
    public void finishCheckState(KickerEvent event) {
        event.setEventState(EventState.FINISH_CHECKING);
    }

    @Override
    public void finishedState(KickerEvent event) {
        event.setEventState(EventState.FINISHED);
    }
}
