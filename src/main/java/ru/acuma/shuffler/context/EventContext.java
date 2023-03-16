package ru.acuma.shuffler.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.EventMapper;
import ru.acuma.shuffler.model.dto.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.repository.EventRepository;
import ru.acuma.shufflerlib.model.Discipline;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventContext {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    @Qualifier("eventStorage")
    private final Map<Long, TgEvent> eventStorage;

    public TgEvent createEvent(Long chatId, Discipline discipline) {
        var event = buildEvent(chatId, discipline);
        eventStorage.put(chatId, event);

        return event;
    }

    public TgEvent findEvent(Long chatId) {
        return eventStorage.get(chatId);
    }

    public boolean isActive(Long chatId) {
        return eventStorage.containsKey(chatId);
    }

    public void sweepEvent(Long chatId) {
        eventStorage.remove(chatId);
    }

    @Deprecated
    public TgEvent update(TgEvent tgEvent) {
        var mapped = eventMapper.toEvent(tgEvent);
        eventRepository.save(mapped);

        return tgEvent;
    }

    private TgEvent buildEvent(Long chatId, Discipline discipline) {
        return TgEvent.builder()
            .eventState(EventState.CREATED)
            .chatId(chatId)
            .startedAt(LocalDateTime.now())
            .discipline(discipline)
            .build();
    }
}
