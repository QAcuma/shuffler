package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.context.StorageContext;
import ru.acuma.shuffler.model.constant.ExecutionStatus;

@Service
@RequiredArgsConstructor
public class DataService {

    private final StorageContext storageContext;
    private final StorageExecutorStrategy<Storable> storageExecutorStrategy;

    @Transactional
    public void saveData(final Long chatId) {
        var eventStorage = storageContext.forChat(chatId);
        eventStorage.getTasks().values()
            .stream()
            .filter(task -> ExecutionStatus.PENDING.equals(task.getExecutionStatus()))
            .map(task -> task.setChatId(chatId))
            .peek(task -> storageExecutorStrategy.getStore(task.getTaskType()).store(task))
            .forEach(task -> task.setExecutionStatus(ExecutionStatus.EXECUTED));
    }
}
