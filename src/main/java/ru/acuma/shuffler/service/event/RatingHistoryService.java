package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.RatingHistoryMapper;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.constant.GameStatus;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.repository.RatingHistoryRepository;
import ru.acuma.shuffler.service.season.SeasonService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingHistoryService {

    private final RatingHistoryMapper ratingHistoryMapper;
    private final RatingHistoryRepository ratingHistoryRepository;
    private final SeasonService seasonService;
    private final GameService gameService;
    private final PlayerService playerService;

    public void logHistory(final TGame game, final Discipline discipline) {
        Optional.of(game)
            .filter(finishedGame -> GameStatus.FINISHED.equals(game.getStatus()))
            .ifPresent(finishedGame -> finishedGame.getPlayers()
                .forEach(player -> logPlayerGame(player, game, discipline))
            );
    }

    private void logPlayerGame(final TEventPlayer player, final TGame game, final Discipline discipline) {
        var seasonReference = seasonService.getReference();
        var gameReference = gameService.getReference(game.getId());
        var playerReference = playerService.getReference(player.getId());
        var historyRecord = ratingHistoryMapper.toHistoryRecord(player.getRatingContext(), playerReference, gameReference, seasonReference, discipline);

        ratingHistoryRepository.save(historyRecord);
    }
}
