package ru.acuma.shuffler.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.EventMapper;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.service.EventContextService;
import ru.acuma.shuffler.service.SeasonService;
import ru.acuma.shufflerlib.model.Discipline;
import ru.acuma.shufflerlib.repository.EventRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventContextServiceImpl implements EventContextService {

    private final EventContext eventContext;
    private final EventMapper eventMapper;
    private final SeasonService seasonService;
    private final EventRepository eventRepository;

    public TgEvent buildEvent(Long chatId, Discipline discipline) {
        if (isActive(chatId)) {
            return getCurrentEvent(chatId);
        }
        TgEvent event = TgEvent.builder()
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

    public TgEvent getCurrentEvent(Long chatId) {
        return eventContext.getEvents().get(chatId);
    }

    @Override
    public Discipline getCurrentDiscipline(Long chatId) {
        return getCurrentEvent(chatId).getDiscipline();
    }

    @Override
    public void evictEvent(Long chatId) {
        eventContext.getEvents().remove(chatId);
    }

    @Override
    public TgEvent update(TgEvent tgEvent) {
        var mapped = eventMapper.toEvent(tgEvent);
        eventRepository.update(mapped);

        return tgEvent;
    }

    private TgEvent cacheEvent(TgEvent tgEvent) {
        eventContext.getEvents().put(tgEvent.getChatId(), tgEvent);
        var mapped = eventMapper.toEvent(tgEvent);
        mapped.setSeasonId(seasonService.getCurrentSeason().getId());
        tgEvent.setId(eventRepository.save(mapped));

        return tgEvent;
    }
}
