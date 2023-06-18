package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.constant.StorageTaskType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageExecutorStrategy<T extends Storable> {

    private final List<StorageExecutor<T>> executors;

    public StorageExecutor<T> getStore(final StorageTaskType taskType) {
        return executors.stream()
            .filter(executor -> executor.getTaskType().equals(taskType))
            .findFirst()
            .orElseThrow(() -> new DataException(ExceptionCause.STORABLE_TASK_NOT_IMPLEMENTED, taskType));
    }
}
