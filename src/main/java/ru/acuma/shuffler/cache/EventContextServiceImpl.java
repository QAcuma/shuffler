package ru.acuma.shuffler.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.EventMapper;
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.service.EventContextService;
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

    public GameEvent buildEvent(Long chatId, Discipline discipline) {
        if (isActive(chatId)) {
            return getCurrentEvent(chatId);
        }
        GameEvent event = GameEvent.builder()
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

    public GameEvent getCurrentEvent(Long chatId) {
        return eventContext.getEvents().get(chatId);
    }

    @Override
    public void evictEvent(Long chatId) {
        eventContext.getEvents().remove(chatId);
    }

    private GameEvent cacheEvent(GameEvent gameEvent) {
        eventContext.getEvents().put(gameEvent.getChatId(), gameEvent);
        var mapped = eventMapper.toEvent(gameEvent);
        mapped.setSeasonId(seasonDao.getCurrentSeason().getId());
        gameEvent.setId(eventDao.save(mapped));

        return gameEvent;
    }
}
