package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.service.event.GameService;

@Service
@RequiredArgsConstructor
public class GameBegins extends StorageExecutor<TGame> {

    private final GameService gameService;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.GAME_BEGINS;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void store(final StorageTask<TGame> storageTask) {
        var event = eventContext.findEvent(storageTask.getChatId());
        gameService.save(storageTask.getSubject(), event.getId());
    }
}
