package ru.acuma.shuffler.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.mapper.GameMapper;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.repository.GameRepository;
import ru.acuma.shuffler.repository.ReferenceService;
import ru.acuma.shuffler.service.event.GameService;
import ru.acuma.shuffler.service.event.RatingService;

@Service
@RequiredArgsConstructor
public class PlayerJoined extends Storable<TEventPlayer> {

    private final GameService gameService;
    private final EventContext eventContext;
    private final GameMapper gameMapper;
    private final GameRepository gameRepository;
    private final RatingService ratingService;
    private final ReferenceService referenceService;
    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.PLAYER_JOINED;
    }

    @Override
    public void store(final StorageTask<TEventPlayer> storageTask) {


    }
}
