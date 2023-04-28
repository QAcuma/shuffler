package ru.acuma.shuffler.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.EventMapper;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.repository.EventRepository;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventContext {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final Map<Long, TEvent> eventStorage = new ConcurrentHashMap<>();
    @Qualifier("redisEventSnapshotStorage")
    private final Map<Long, TEvent> eventSnapshotStorage;

    public TEvent createEvent(final Long chatId, final Discipline discipline) {
        var event = eventMapper.initEvent(chatId, discipline);
        eventStorage.put(chatId, event);

        return event;
    }

    public TEvent findEvent(final Long chatId) {
        return eventStorage.get(chatId);
    }

    public boolean isActive(final Long chatId) {
        return eventStorage.containsKey(chatId);
    }

    public void flushEvent(final Long chatId) {
        eventStorage.remove(chatId);
    }

    /**
     * Создает снепшот эвента перед внесением изменений в него
     */
    public void snapshotEvent(final Long chatId) {
        Optional.ofNullable(findEvent(chatId))
            .ifPresent(event -> eventSnapshotStorage.put(chatId, event));
    }

    /**
     * Откатывает версию эвента до последней успешной для чата
     */
    public void rollbackEvent(final Long chatId) {
        Optional.ofNullable(eventSnapshotStorage.get(chatId))
            .ifPresent(event -> eventStorage.put(chatId, event));
    }
}
