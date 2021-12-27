package ru.acuma.k.shuffler.cache;

import org.springframework.stereotype.Component;
import ru.acuma.k.shuffler.model.domain.KickerEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventContext {

    private final Map<Long, KickerEvent> activeEvents = new ConcurrentHashMap<>();

    protected Map<Long, KickerEvent> getEvents() {
        return activeEvents;
    }
}
