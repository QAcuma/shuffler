package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.mapper.EventMapper;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.model.entity.Season;
import ru.acuma.shuffler.repository.EventRepository;
import ru.acuma.shuffler.repository.ReferenceService;
import ru.acuma.shuffler.service.season.SeasonService;

@Service
@RequiredArgsConstructor
public class EventBegins extends StorageExecutor<TEvent> {

    private final EventMapper eventMapper;
    private final SeasonService seasonService;
    private final EventRepository eventRepository;
    private final ReferenceService referenceService;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.EVENT_BEGIN;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void store(final StorageTask<TEvent> storageTask) {
        var chatEvent = storageTask.getSubject();
        var chat = referenceService.getReference(GroupInfo.class, chatEvent.getChatId());
        var season = referenceService.getReference(Season.class, seasonService.getSeasonId());
        var event = eventMapper.toEvent(chatEvent, chat, season);

        eventRepository.save(event);
        chatEvent.setId(event.getId());
    }
}
