package ru.acuma.shuffler.context.cotainer;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.model.constant.ExecutionStatus;
import ru.acuma.shuffler.model.constant.StorageTaskType;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
public class StorageTask<T> implements Serializable {

    private Long chatId;
    private transient T subject;
    private StorageTaskType taskType;
    private ExecutionStatus executionStatus;

    public static <T> StorageTask<T> of(final StorageTaskType taskType, final T entity) {
        return StorageTask.<T>builder()
            .executionStatus(ExecutionStatus.PENDING)
            .taskType(taskType)
            .subject(entity)
            .build();
    }
}
