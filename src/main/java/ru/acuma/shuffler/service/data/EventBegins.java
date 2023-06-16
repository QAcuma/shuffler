package ru.acuma.shuffler.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.mapper.EventMapper;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.repository.EventRepository;
import ru.acuma.shuffler.service.season.SeasonService;
import ru.acuma.shuffler.service.telegram.ChatService;

@Service
@RequiredArgsConstructor
public class EventBegins extends Storable<TEvent> {

    private final EventMapper eventMapper;
    private final SeasonService seasonService;
    private final ChatService chatService;
    private final EventRepository eventRepository;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.EVENT_BEGIN;
    }

    @Override
    public void store(final StorageTask<TEvent> storageTask) {
        var chatEvent = storageTask.getSubject();
        var chat = chatService.getGroupInfo(chatEvent.getChatId());
        var season = seasonService.getCurrentSeason();
        var event = eventMapper.toEvent(chatEvent, chat, season);

        eventRepository.save(event);
        chatEvent.setId(event.getId());
    }
}
