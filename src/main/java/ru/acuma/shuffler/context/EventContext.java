package ru.acuma.shuffler.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.EventMapper;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.repository.EventRepository;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventContext {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    @Qualifier("redisEventStorage")
    private final Map<Long, TgEvent> eventStorage;
    @Qualifier("redisEventSnapshotStorage")
    private final Map<Long, TgEvent> eventSnapshotStorage;

    public TgEvent createEvent(Long chatId, Discipline discipline) {
        var event = eventMapper.initEvent(chatId, discipline);
        eventStorage.put(chatId, event);

        return event;
    }

    public TgEvent findEvent(Long chatId) {
        return eventStorage.get(chatId);
    }

    public boolean isActive(Long chatId) {
        return eventStorage.containsKey(chatId);
    }

    public void flushEvent(Long chatId) {
        eventStorage.remove(chatId);
    }

    /**
     * Создает снепшот эвента перед внесением изменений в него
     */
    public TgEvent snapshotEvent(Long chatId) {
        var event = findEvent(chatId);
        eventSnapshotStorage.put(chatId, event);

        return event;
    }

    /**
     * Откатывает версию эвента до последней успешной для чата
     */
    public void rollbackEvent(Long chatId) {
        var event = eventSnapshotStorage.get(chatId);
        eventStorage.put(chatId, event);
    }
}
