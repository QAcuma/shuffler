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
public class StorageTask implements Serializable {

    private Long entityId;
    private StorageTaskType taskType;
    private ExecutionStatus executionStatus;

    public static StorageTask of(final StorageTaskType taskType) {
        return StorageTask.builder()
            .executionStatus(ExecutionStatus.PENDING)
            .taskType(taskType)
            .build();
    }

    public static StorageTask of(final StorageTaskType taskType, final Long entityId) {
        return StorageTask.builder()
            .executionStatus(ExecutionStatus.PENDING)
            .taskType(taskType)
            .entityId(entityId)
            .build();
    }
}
