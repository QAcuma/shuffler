package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventService;

@Service
@RequiredArgsConstructor
public class EventBegins extends StorageExecutor<TEvent> {

    private final EventService eventService;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.EVENT_BEGIN;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void store(final StorageTask<TEvent> storageTask) {
        var event = storageTask.getSubject();
        eventService.save(event);
    }
}
