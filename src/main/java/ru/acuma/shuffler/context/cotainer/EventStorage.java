package ru.acuma.shuffler.context.cotainer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.service.storage.Storable;

import java.util.EnumMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class EventStorage {

    private final Map<StorageTaskType, StorageTask<Storable>> tasks = new EnumMap<>(StorageTaskType.class);

    public EventStorage store(final StorageTask<Storable> task) {
        tasks.put(task.getTaskType(), task);

        return this;
    }

    public static EventStorage of() {
        return new EventStorage();
    }

    private EventStorage() {
    }
}
