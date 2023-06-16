package ru.acuma.shuffler.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.mapper.GameMapper;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.service.event.GameService;

@Service
@RequiredArgsConstructor
public class GameFinished extends Storable<TGame> {

    private final GameService gameService;
    private final GameMapper gameMapper;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.GAME_FINISHED;
    }

    @Override
    public void store(final StorageTask<TGame> storageTask) {
        var finishedGame = storageTask.getSubject();
        var game = gameService.findGame(finishedGame.getId());

        gameMapper.updateGame(game, finishedGame);
    }
}
