package ru.acuma.shuffler.service.data;

import org.springframework.beans.factory.annotation.Autowired;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.model.constant.StorageTaskType;

public abstract class Storable {

    @Autowired
    protected EventContext eventContext;

    abstract StorageTaskType getTaskType();
    abstract void store(StorageTask storageTask);
}
