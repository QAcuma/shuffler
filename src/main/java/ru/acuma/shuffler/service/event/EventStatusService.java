package ru.acuma.shuffler.service.event;

import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;

import java.time.LocalDateTime;

@Service
public class EventStatusService {

    public EventStatus praperation(final TEvent event) {
        if (event.isPlayersEnough()) {
            return ready(event);
        }
        return created(event);
    }

    public EventStatus resume(final TEvent event) {
        if (event.isPlayersEnough()) {
            return playing(event);
        }
        if (event.getCurrentGame().isActive()) {
            return waitingWithGame(event);
        }
        return waiting(event);
    }

    public void beginChecking(final TEvent event) {
        event.setEventStatus(EventStatus.BEGIN_CHECKING);
    }

    public void cancelCheck(final TEvent event) {
        event.setEventStatus(EventStatus.CANCEL_CHECKING);
    }

    public void gameCheck(final TEvent event) {
        event.setEventStatus(EventStatus.GAME_CHECKING);
    }

    public void cancelled(final TEvent event) {
        event.setEventStatus(EventStatus.CANCELLED);
    }

    public void begin(final TEvent event) {
        event.setEventStatus(EventStatus.BEGIN_CHECKING);
    }

    public void evicting(final TEvent event) {
        event.setEventStatus(EventStatus.EVICTING);
    }

    public void finishCheck(final TEvent event) {
        event.setEventStatus(EventStatus.FINISH_CHECKING);
    }

    public void finished(final TEvent event) {
        event.setEventStatus(EventStatus.FINISHED);
        event.setFinishedAt(LocalDateTime.now());
    }

    private EventStatus created(final TEvent event) {
        return event.setEventStatus(EventStatus.CREATED).getEventStatus();
    }

    private EventStatus ready(final TEvent event) {
        return event.setEventStatus(EventStatus.READY).getEventStatus();
    }

    private EventStatus waiting(final TEvent event) {
        return event.setEventStatus(EventStatus.WAITING).getEventStatus();
    }

    private EventStatus playing(final TEvent event) {
        return event.setEventStatus(EventStatus.PLAYING).getEventStatus();
    }

    private EventStatus waitingWithGame(final TEvent event) {
        return event.setEventStatus(EventStatus.WAITING_WITH_GAME).getEventStatus();
    }
}
