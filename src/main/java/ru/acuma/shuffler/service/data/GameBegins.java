package ru.acuma.shuffler.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.GameMapper;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.repository.EventRepository;
import ru.acuma.shuffler.repository.GameRepository;

@Service
@RequiredArgsConstructor
public class GameBegins extends Storable {

    private final GameMapper gameMapper;
    private final GameRepository gameRepository;
    private final EventRepository eventRepository;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.GAME_BEGINS;
    }

    @Override
    public void store(final StorageTask storageTask) {
        var event = eventContext.findEvent(storageTask.getChatId());
        var persistentEvent = eventRepository.findById(event.getId())
            .orElseThrow(() -> new DataException(ExceptionCause.EVENT_NOT_FOUND, event.getId()));
        var game = gameMapper.toGame(event.getCurrentGame(), persistentEvent);

        gameRepository.save(game);
    }
}
