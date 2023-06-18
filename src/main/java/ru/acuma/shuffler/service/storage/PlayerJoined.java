package ru.acuma.shuffler.service.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.mapper.RatingMapper;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.repository.RatingRepository;
import ru.acuma.shuffler.repository.ReferenceService;

@Service
@RequiredArgsConstructor
public class PlayerJoined extends StorageExecutor<TEventPlayer> {

    private final RatingMapper ratingMapper;
    private final ReferenceService referenceService;
    private final RatingRepository ratingRepository;

    @Override
    public StorageTaskType getTaskType() {
        return StorageTaskType.PLAYER_JOINED;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void store(final StorageTask<TEventPlayer> storageTask) {
        var event = eventContext.findEvent(storageTask.getChatId());
        var player = storageTask.getSubject();
        var rating = player.getRatingContext();
        var mappedRating = ratingMapper.toRating(
            rating,
            referenceService.getReference(Player.class, player.getId()),
            event.getDiscipline()
        );

        ratingRepository.save(mappedRating);
        rating.setId(mappedRating.getId());
    }
}
