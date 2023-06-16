package ru.acuma.shuffler.service.data;

import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TEvent;

@Service
public class EventFinished extends Storable<TEvent> {
    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.EVENT_FINISHED;
    }

    @Override
    public void store(final StorageTask<TEvent> storageTask) {

    }
}
