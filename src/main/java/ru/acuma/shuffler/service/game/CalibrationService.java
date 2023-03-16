package ru.acuma.shuffler.service.game;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.entity.RatingHistory;
import ru.acuma.shuffler.repository.RatingHistoryRepository;
import ru.acuma.shuffler.service.season.SeasonService;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalibrationService {

    private final RatingHistoryRepository ratingHistoryRepository;
    private final SeasonService seasonService;

    @Value("${rating.calibration.games}")
    private int requiredGames;

    @Value("${rating.calibration.enemies}")
    private int requiredEnemies;

    public int getDistinctEnemiesCount(Long playerId, Discipline discipline) {
        var changes = ratingHistoryRepository.findBySeasonIdAndPlayerIdAndDiscipline(
            seasonService.getCurrentSeason().getId(),
            playerId,
            discipline
        );
        var playerHistory = changes.stream()
            .filter(change -> change.getPlayer().getId().equals(playerId))
            .toList();

        return Math.toIntExact(playerHistory.stream()
            .flatMap(change -> matesFilter(change, changes).stream())
            .map(RatingHistory::getPlayer)
            .distinct()
            .count());
    }

    public boolean isCalibrated(Long playerId, Discipline discipline) {
        var games = ratingHistoryRepository.findBySeasonIdAndPlayerIdAndDiscipline(
            seasonService.getCurrentSeason().getId(),
            playerId,
            discipline
        );
        int distinctEnemies = getDistinctEnemiesCount(playerId, discipline);

        return games.size() > requiredGames && distinctEnemies >= requiredEnemies;
    }

    private List<RatingHistory> matesFilter(RatingHistory history, List<RatingHistory> changes) {
        return changes.stream()
            .filter(change -> change.getGame().equals(history.getGame().getId()))
            .filter(change -> !change.getChange().equals(history.getChange()))
            .toList();
    }

}
