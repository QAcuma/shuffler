package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TgGame;
import ru.acuma.shuffler.model.enums.TeamStatus;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.service.CalibrationService;
import ru.acuma.shuffler.service.RatingService;
import ru.acuma.shuffler.service.SeasonService;
import ru.acuma.shuffler.tables.pojos.Rating;
import ru.acuma.shuffler.tables.pojos.RatingHistory;
import ru.acuma.shufflerlib.model.Discipline;
import ru.acuma.shufflerlib.model.Filter;
import ru.acuma.shufflerlib.repository.RatingHistoryRepository;
import ru.acuma.shufflerlib.repository.RatingRepository;

import javax.management.InstanceNotFoundException;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final SeasonService seasonService;
    private final RatingHistoryRepository ratingHistoryRepository;
    private final RatingRepository ratingRepository;
    private final CalibrationService calibrationService;

    @Value("${rating.calibration.game-penalty}")
    private double calibrationPenaltyMultiplier;

    @Override
    @SneakyThrows
    @Transactional
    public void update(TgEvent event) {
        var game = event.getCurrentGame();
        Optional.ofNullable(game.getWinnerTeam()).orElseThrow(() -> new InstanceNotFoundException("Отсутствует победившая команда"));
        var changes = calcChanges(game);

        applyChanges(game, changes);
        saveChanges(event);
    }

    private int calcChanges(TgGame game) {
        var winnerBet = game.getWinnerTeam().getBet();
        var loserBet = game.getLoserTeam().getBet();
        var diff = game.getWinnerTeam().getScore() - game.getLoserTeam().getScore();
        var factor = diff > 0 ? TeamStatus.STRONG : TeamStatus.WEAK;
        var change = Values.BASE_RATING_CHANGE * (factor.getFactor() + ((double) diff / Values.RATING_REFERENCE));
        var modChange = game.isCalibrating() ? change * calibrationPenaltyMultiplier : change;

        return limitAndRoundChange(modChange);
    }

    @Override
    public void defaultRating(Long playerId) {
        Arrays.stream(Discipline.values()).forEach(discipline -> defaultRating(playerId, discipline));
    }

    @Override
    public Rating defaultRating(Long playerId, Discipline discipline) {
        return newDefaultRating(playerId, discipline);
    }

    @Override
    public Rating getRating(Long playerId, Discipline discipline) {
        Filter filter = new Filter()
                .setPlayerId(playerId)
                .setDiscipline(discipline)
                .setSeasonId(seasonService.getCurrentSeason().getId());
        Rating rating = ratingRepository.getRatingByPlayerIdAndDisciplineAndSeasonId(filter);

        return rating == null
                ? defaultRating(playerId, discipline)
                : rating;
    }

    @Override
    public void saveChanges(TgEvent event) {
        TgGame game = event.getCurrentGame();
        event.getCurrentGame().getPlayers()
                .stream()
                .peek(player -> updateRating(player, event.getDiscipline()))
                .forEach(player -> logHistory(
                        player,
                        game.getId(),
                        getRatingChange(player, game)
                ));
    }

    private void logHistory(TgEventPlayer player, Long gameId, Integer ratingChange) {
        RatingHistory ratingHistory = new RatingHistory()
                .setGameId(gameId)
                .setPlayerId(player.getId())
                .setChange(ratingChange)
                .setSeasonId(seasonService.getCurrentSeason().getId())
                .setScore(player.getScore());

        ratingHistoryRepository.save(ratingHistory);
    }

    private Integer getRatingChange(TgEventPlayer player, TgGame game) {
        var winnerTeam = game.getWinnerTeam();

        return winnerTeam.getPlayers().contains(player)
                ? winnerTeam.getRatingChange()
                : - winnerTeam.getRatingChange();
    }

    @Override
    public void updateRating(TgEventPlayer player, Discipline discipline) {
        Rating rating = getRating(player.getId(), discipline);

        rating.setScore(player.getScore());
        rating.setIsCalibrated(calibrationService.isCalibrated(player.getId()));
        player.setCalibrated(rating.getIsCalibrated());

        ratingRepository.update(rating);
    }

    private Rating newDefaultRating(Long playerId, Discipline discipline) {
        Rating rating = new Rating()
                .setDiscipline(discipline.name())
                .setSeasonId(seasonService.getCurrentSeason().getId())
                .setPlayerId(playerId)
                .setScore(Values.DEFAULT_RATING)
                .setIsCalibrated(false);

        return rating.setId(ratingRepository.save(rating));
    }

    private void applyChanges(TgGame game, int changes) {
        game.getWinnerTeam().setRatingChange(changes);
        game.getLoserTeam().setRatingChange(-changes);
        game.getWinnerTeam().getPlayers().forEach(player -> player.plusRating(changes));
        game.getLoserTeam().getPlayers().forEach(player -> player.minusRating(changes));
    }

    private int limitAndRoundChange(double change) {
        int value = Math.toIntExact(Math.round(change));
        if (value >= Values.BASE_RATING_CHANGE * 2) {
            return (Values.BASE_RATING_CHANGE * 2) - 1;
        }
        return Math.max(value, 1);
    }

}
