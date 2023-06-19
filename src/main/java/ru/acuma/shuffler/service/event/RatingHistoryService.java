package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.mapper.RatingHistoryMapper;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.repository.RatingHistoryRepository;
import ru.acuma.shuffler.service.season.SeasonService;

@Service
@RequiredArgsConstructor
public class RatingHistoryService {

    private final RatingHistoryMapper ratingHistoryMapper;
    private final RatingHistoryRepository ratingHistoryRepository;
    private final SeasonService seasonService;
    public void logHistory(final TGame game, final Discipline discipline) {
        game.getPlayers().forEach(player -> logPlayerGame(player, game, discipline));
    }

    private void logPlayerGame(final TEventPlayer player, final TGame game, final Discipline discipline) {
        var season = seasonService.getReference();
        var historyRecord = ratingHistoryMapper.toHistoryRecord(player, game, season, discipline);

        ratingHistoryRepository.save(historyRecord);
    }
}
