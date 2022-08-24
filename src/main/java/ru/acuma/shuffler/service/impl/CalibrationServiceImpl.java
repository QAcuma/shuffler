package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.service.CalibrationService;
import ru.acuma.shuffler.service.SeasonService;
import ru.acuma.shuffler.tables.pojos.RatingHistory;
import ru.acuma.shufflerlib.repository.RatingHistoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalibrationServiceImpl implements CalibrationService {

    private final RatingHistoryRepository ratingHistoryRepository;
    private final SeasonService seasonService;

    @Override
    public Long getPlayerEnemiesCount(Long playerId) {
        var changes = ratingHistoryRepository.findHistoryBySeasonIdAndPlayerId(
                seasonService.getCurrentSeason().getId(),
                playerId
        );
        var playerHistory = changes.stream()
                .filter(change -> change.getPlayerId().equals(playerId))
                .collect(Collectors.toList());

        return playerHistory.stream()
                .flatMap(change -> matesFilter(change, changes).stream())
                .map(RatingHistory::getPlayerId)
                .distinct()
                .count();
    }

    private List<RatingHistory> matesFilter(RatingHistory history, List<RatingHistory> changes) {
        return changes.stream()
                .filter(change -> change.getGameId().equals(history.getGameId()))
                .filter(change -> change.getChange().equals(history.getChange()))
                .collect(Collectors.toList());
    }

}
