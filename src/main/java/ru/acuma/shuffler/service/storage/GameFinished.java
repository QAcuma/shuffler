package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.service.event.GameService;
import ru.acuma.shuffler.service.event.RatingHistoryService;
import ru.acuma.shuffler.service.event.RatingService;

@Service
@RequiredArgsConstructor
public class GameFinished extends StorageExecutor<TGame> {

    private final GameService gameService;
    private final RatingService ratingService;
    private final RatingHistoryService ratingHistoryService;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.GAME_FINISHED;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void store(final StorageTask<TGame> storageTask) {
        var event = eventContext.findEvent(storageTask.getChatId());
        var finishedGame = storageTask.getSubject();
        finishedGame.getPlayers().stream()
            .map(TEventPlayer::getRatingContext)
            .forEach(ratingService::update);
        gameService.update(finishedGame);
        ratingHistoryService.logHistory(finishedGame, event.getDiscipline());
    }
}
