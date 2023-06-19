package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.EventMapper;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.entity.Event;
import ru.acuma.shuffler.repository.EventRepository;
import ru.acuma.shuffler.repository.ReferenceService;
import ru.acuma.shuffler.service.season.SeasonService;
import ru.acuma.shuffler.service.storage.Referenced;
import ru.acuma.shuffler.service.telegram.ChatService;

@Service
@RequiredArgsConstructor
public class EventService implements Referenced<Event> {

    private final EventRepository eventRepository;
    private final ChatService chatService;
    private final SeasonService seasonService;
    private final EventMapper eventMapper;
    private final ReferenceService referenceService;

    @Transactional(propagation = Propagation.MANDATORY)
    public Event findEvent(final Long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new DataException(ExceptionCause.EVENT_NOT_FOUND, eventId));
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Event getReference(final Long eventId) {
        return referenceService.getReference(Event.class, eventId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void update(final TEvent event) {
        var savedEvent = findEvent(event.getId());
        eventMapper.update(savedEvent, event);
    }

    @Transactional
    public void save(final TEvent event) {
        var chat = chatService.getReference(event.getChatId());
        var season = seasonService.getReference();
        var mappedEvent = eventMapper.toEvent(event, chat, season);

        eventRepository.save(mappedEvent);
        event.setId(mappedEvent.getId());
    }
}
