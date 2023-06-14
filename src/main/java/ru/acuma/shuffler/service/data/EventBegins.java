package ru.acuma.shuffler.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.mapper.EventMapper;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.repository.EventRepository;
import ru.acuma.shuffler.service.season.SeasonService;
import ru.acuma.shuffler.service.telegram.ChatService;

@Service
@RequiredArgsConstructor
public class EventBegins extends Storable {

    private final EventMapper eventMapper;
    private final SeasonService seasonService;
    private final ChatService chatService;
    private final EventRepository eventRepository;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.EVENT_BEGIN;
    }

    @Override
    public void store(final StorageTask storageTask) {
        var event = eventContext.findEvent(storageTask.getChatId());
        var chat = chatService.getGroupInfo(event.getChatId());
        var season = seasonService.getCurrentSeason();
        var mappedEvent = eventMapper.toEvent(event, chat, season);

        eventRepository.save(mappedEvent);
    }
}
