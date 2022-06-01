package ru.acuma.shuffler.cache;

import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.GameEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventContext {

    private final Map<Long, GameEvent> activeEvents = new ConcurrentHashMap<>();

    protected Map<Long, GameEvent> getEvents() {
        return activeEvents;
    }
}
