package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.service.event.RatingService;

@Service
@RequiredArgsConstructor
public class PlayerJoined extends StorageExecutor<TEventPlayer> {

    private final RatingService ratingService;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.PLAYER_JOINED;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void store(final StorageTask<TEventPlayer> storageTask) {
        var event = eventContext.findEvent(storageTask.getChatId());
        var player = storageTask.getSubject();

        ratingService.save(player, event.getDiscipline());
    }
}
