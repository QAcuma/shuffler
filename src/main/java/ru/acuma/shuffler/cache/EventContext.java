package ru.acuma.shuffler.cache;

import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.TgEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventContext {

    private final Map<Long, TgEvent> activeEvents = new ConcurrentHashMap<>();

    protected Map<Long, TgEvent> getEvents() {
        return activeEvents;
    }
}
