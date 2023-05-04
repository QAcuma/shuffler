package ru.acuma.shuffler.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.mapper.EventMapper;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.repository.EventRepository;
import ru.acuma.shuffler.service.season.SeasonService;
import ru.acuma.shuffler.model.constant.Discipline;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventContext {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final SeasonService seasonService;
    private final Map<Long, TEvent> eventStorage = new ConcurrentHashMap<>();
    @Qualifier("redisEventSnapshotStorage")
    private final Map<Long, TEvent> eventSnapshotStorage;

    public TEvent createEvent(final Long chatId, final Discipline discipline) {
        var event = eventMapper.initEvent(chatId, discipline);
        eventStorage.put(chatId, event);

        return event;
    }

    public TEvent findEvent(final Long chatId) {
        return Optional.ofNullable(eventStorage.get(chatId))
            .orElseGet(() -> rollbackEvent(chatId));
    }

    public boolean isActive(final Long chatId) {
        return eventStorage.containsKey(chatId);
    }

    public void flushEvent(final Long chatId) {
        eventStorage.remove(chatId);
        eventSnapshotStorage.remove(chatId);
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
    public TEvent rollbackEvent(final Long chatId) {
        return Optional.ofNullable(eventSnapshotStorage.get(chatId))
            .map(event -> eventStorage.put(chatId, event))
            .orElse(null);
    }
}
