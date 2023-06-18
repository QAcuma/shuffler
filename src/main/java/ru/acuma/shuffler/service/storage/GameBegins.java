package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.mapper.GameMapper;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.entity.Event;
import ru.acuma.shuffler.repository.ReferenceService;
import ru.acuma.shuffler.service.event.GameService;

@Service
@RequiredArgsConstructor
public class GameBegins extends StorageExecutor<TGame> {

    private final GameMapper gameMapper;
    private final GameService gameService;
    private final ReferenceService referenceService;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.GAME_BEGINS;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void store(final StorageTask<TGame> storageTask) {
        var event = eventContext.findEvent(storageTask.getChatId());
        var eventReference = referenceService.getReference(Event.class, event.getId());
        var currentGame = storageTask.getSubject();
        var game = gameMapper.toGame(currentGame, eventReference);

        gameService.save(game, currentGame);
    }
}
