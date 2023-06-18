package ru.acuma.shuffler.context.cotainer;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.model.constant.ExecutionStatus;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.service.storage.Storable;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
public class StorageTask<T extends Storable> implements Serializable {

    private Long chatId;
    private transient T subject;
    private StorageTaskType taskType;
    private ExecutionStatus executionStatus;

    public static StorageTask<Storable> of(final StorageTaskType taskType, final Storable entity) {
        return StorageTask.<Storable>builder()
            .executionStatus(ExecutionStatus.PENDING)
            .taskType(taskType)
            .subject(entity)
            .build();
    }
}
