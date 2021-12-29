package ru.acuma.k.shuffler.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.service.EventContextService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventContextServiceImpl implements EventContextService {

    private final EventContext eventContext;

    public KickerEvent buildEvent(Long chatId) {
        if (isActive(chatId)) {
            return getEvent(chatId);
        }
        KickerEvent event = KickerEvent.builder()
                .eventState(EventState.CREATED)
                .chatId(chatId)
                .startedAt(LocalDateTime.now())
                .build();
        return cacheEvent(event);
    }

    public boolean isActive(Long chatId) {
        final var event = getEvent(chatId);
        if (event != null) {
            return !event.getEventState().equals(EventState.FINISHED);
        }
        return false;
    }

    public KickerEvent getEvent(Long chatId) {
        return eventContext.getEvents().get(chatId);
    }

    @Override
    public void evictEvent(Long chatId) {
        eventContext.getEvents().remove(chatId);
    }

    private KickerEvent cacheEvent(KickerEvent event) {
        eventContext.getEvents().put(event.getChatId(), event);
        return event;
    }
}
