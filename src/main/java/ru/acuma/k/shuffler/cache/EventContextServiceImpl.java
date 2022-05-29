package ru.acuma.k.shuffler.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.mapper.EventMapper;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.service.EventContextService;
import ru.acuma.shufflerlib.dao.EventDao;
import ru.acuma.shufflerlib.dao.SeasonDao;
import ru.acuma.shufflerlib.model.Discipline;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventContextServiceImpl implements EventContextService {

    private final EventContext eventContext;
    private final EventMapper eventMapper;
    private final SeasonDao seasonDao;
    private final EventDao eventDao;

    public KickerEvent buildEvent(Long chatId, Discipline discipline) {
        if (isActive(chatId)) {
            return getCurrentEvent(chatId);
        }
        KickerEvent event = KickerEvent.builder()
                .eventState(EventState.CREATED)
                .chatId(chatId)
                .startedAt(LocalDateTime.now())
                .discipline(discipline)
                .build();

        return cacheEvent(event);
    }

    public boolean isActive(Long chatId) {
        final var event = getCurrentEvent(chatId);
        if (event != null) {
            return !event.getEventState().equals(EventState.FINISHED);
        }
        return false;
    }

    public KickerEvent getCurrentEvent(Long chatId) {
        return eventContext.getEvents().get(chatId);
    }

    @Override
    public void evictEvent(Long chatId) {
        eventContext.getEvents().remove(chatId);
    }

    private KickerEvent cacheEvent(KickerEvent kickerEvent) {
        eventContext.getEvents().put(kickerEvent.getChatId(), kickerEvent);
        var mapped = eventMapper.toEvent(kickerEvent);
        mapped.setSeasonId(seasonDao.getCurrentSeason().getId());
        kickerEvent.setId(eventDao.save(mapped));

        return kickerEvent;
    }
}
