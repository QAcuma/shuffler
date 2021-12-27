package ru.acuma.k.shuffler.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.model.enums.EventState;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventContextService {

    private final static int READY_SIZE = 2;

    private final EventContext eventContext;

    public KickerEvent getEvent(Long groupId) {
        return eventContext.getEvents().get(groupId);
    }

    public KickerEvent buildEvent(Long groupId) {
        if (isActive(groupId)) {
            return getEvent(groupId);
        }

        KickerEvent event = KickerEvent.builder()
                .eventState(EventState.CREATED)
                .groupId(groupId)
                .startedAt(LocalDateTime.now())
                .build();
        addEvent(event);

        return event;
    }

    public boolean isActive(Long groupId) {
        var event = getEvent(groupId);
        if (event != null) {
            return !event.getEventState().equals(EventState.FINISHED);
        }
        return false;
    }

    public boolean isUserRegistered(Long groupId, User user) {
        return getEvent(groupId).getMembers().contains(user);
    }

    public void finishEvent(KickerEvent event, boolean store) {
        event.setEventState(EventState.FINISHED);
        event.setFinishedAt(LocalDateTime.now());
        eventContext.getEvents().remove(event.getGroupId());
    }

    public int getReadySize() {
        return READY_SIZE;
    }

    private void addEvent(KickerEvent event) {
        eventContext.getEvents().put(event.getGroupId(), event);
    }
}
