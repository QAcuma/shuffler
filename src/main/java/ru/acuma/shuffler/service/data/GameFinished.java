package ru.acuma.shuffler.service.data;

import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.model.constant.StorageTaskType;

@Service
public class GameFinished extends Storable {
    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.GAME_FINISHED;
    }

    @Override
    public void store(final StorageTask storageTask) {

    }
}
