package ru.acuma.shuffler.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.context.StorageContext;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.constant.ExecutionStatus;
import ru.acuma.shuffler.model.constant.StorageTaskType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {

    private final StorageContext storageContext;
    private final List<Storable> storables;

    @Transactional
    public void saveData(final Long chatId) {
        var eventStorage = storageContext.forChat(chatId);
        eventStorage.getTasks().values()
            .stream()
            .filter(task -> ExecutionStatus.PENDING.equals(task.getExecutionStatus()))
            .map(task -> task.setChatId(chatId))
            .forEach(task -> getStore(task.getTaskType()).store(task));
    }

    private Storable getStore(StorageTaskType taskType) {
        return storables.stream()
            .filter(storable -> storable.getTaskType().equals(taskType))
            .findFirst()
            .orElseThrow(() -> new DataException(ExceptionCause.STORABLE_TASK_NOT_IMPLEMENTED, taskType));
    }
}
