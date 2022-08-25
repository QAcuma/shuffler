package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.service.CalibrationService;
import ru.acuma.shuffler.service.SeasonService;
import ru.acuma.shuffler.tables.pojos.RatingHistory;
import ru.acuma.shufflerlib.repository.RatingHistoryRepository;
import ru.acuma.shufflerlib.repository.RatingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalibrationServiceImpl implements CalibrationService {

    private final RatingHistoryRepository ratingHistoryRepository;
    private final SeasonService seasonService;

    @Value("${rating.calibration.games}")
    private int requiredGames;

    @Value("${rating.calibration.enemies}")
    private int requiredEnemies;

    @Override
    public int getDistinctEnemiesCount(Long playerId) {
        var changes = ratingHistoryRepository.findHistoryBySeasonIdAndPlayerId(
                seasonService.getCurrentSeason().getId(),
                playerId
        );
        var playerHistory = changes.stream()
                .filter(change -> change.getPlayerId().equals(playerId))
                .collect(Collectors.toList());

        return Math.toIntExact(playerHistory.stream()
                .flatMap(change -> matesFilter(change, changes).stream())
                .map(RatingHistory::getPlayerId)
                .distinct()
                .count());
    }

    @Override
    public boolean isCalibrated(Long playerId) {
        int gamesCount = ratingHistoryRepository.findGamesCountBySeasonIdAndPlayerId(
                seasonService.getCurrentSeason().getId(),
                playerId
        );
        int distinctEnemies = getDistinctEnemiesCount(playerId);

        return gamesCount > requiredGames && distinctEnemies > requiredEnemies;
    }

    private List<RatingHistory> matesFilter(RatingHistory history, List<RatingHistory> changes) {
        return changes.stream()
                .filter(change -> change.getGameId().equals(history.getGameId()))
                .filter(change -> !change.getChange().equals(history.getChange()))
                .collect(Collectors.toList());
    }

}
