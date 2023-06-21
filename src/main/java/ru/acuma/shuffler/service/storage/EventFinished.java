package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventService;
import ru.acuma.shuffler.service.event.GameService;

@Service
@RequiredArgsConstructor
public class EventFinished extends StorageExecutor<TEvent> {

    private final GameService gameService;
    private final EventService eventService;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.EVENT_FINISHED;
    }

    @Override
    public void store(final StorageTask<TEvent> storageTask) {
        var event = storageTask.getSubject();
        if (!CollectionUtils.isEmpty(event.getTgGames())) {
            gameService.update(event.getCurrentGame());
        }
        eventService.update(event);
    }
}
