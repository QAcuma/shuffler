package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.service.event.GameService;
import ru.acuma.shuffler.service.event.RatingService;

@Service
@RequiredArgsConstructor
public class EventFinished extends StorageExecutor<TEvent> {

    private final GameService gameService;
    private final RatingService ratingService;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.EVENT_FINISHED;
    }

    @Override
    public void store(final StorageTask<TEvent> storageTask) {
        var finishedGame = storageTask.getSubject().getCurrentGame();
        finishedGame.getPlayers().stream()
            .map(TEventPlayer::getRatingContext)
            .forEach(ratingService::updateRating);
        gameService.updateGame(finishedGame);

    }
}
