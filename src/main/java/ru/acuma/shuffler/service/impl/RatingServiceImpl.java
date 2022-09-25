package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TgGame;
import ru.acuma.shuffler.model.entity.TgGameBet;
import ru.acuma.shuffler.model.entity.TgTeam;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.service.api.CalibrationService;
import ru.acuma.shuffler.service.api.RatingService;
import ru.acuma.shuffler.service.api.SeasonService;
import ru.acuma.shuffler.tables.pojos.Rating;
import ru.acuma.shuffler.tables.pojos.RatingHistory;
import ru.acuma.shufflerlib.model.Discipline;
import ru.acuma.shufflerlib.model.Filter;
import ru.acuma.shufflerlib.repository.RatingHistoryRepository;
import ru.acuma.shufflerlib.repository.RatingRepository;

import javax.management.InstanceNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final SeasonService seasonService;
    private final RatingHistoryRepository ratingHistoryRepository;
    private final RatingRepository ratingRepository;
    private final CalibrationService calibrationService;

    @Value("${rating.calibration.game-penalty}")
    private float calibrationPenaltyMultiplier;

    @Override
    public void applyBet(TgTeam redTeam, TgTeam blueTeam) {
        boolean isCalibrating = redTeam.containsCalibrating() || blueTeam.containsCalibrating();

        var redWinCase = winCase(redTeam, blueTeam);
        var limitedRedWinCase = limitAndRoundChange(redWinCase, isCalibrating);
        var blueWinCase = getBasePool(isCalibrating) - limitedRedWinCase;

        var redLoseCase = -1 * blueWinCase;
        var blueLoseCase = -1 * limitedRedWinCase;

        var redBet = new TgGameBet(limitedRedWinCase, redLoseCase);
        var blueBet = new TgGameBet(blueWinCase, blueLoseCase);

        redTeam.setBet(redBet);
        blueTeam.setBet(blueBet);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void update(TgEvent event) {
        var game = event.getLatestGame();
        Optional.ofNullable(game.getWinnerTeam()).orElseThrow(() -> new InstanceNotFoundException("Отсутствует победившая команда"));
        applyChanges(event);
        saveResults(event);
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
        Filter filter = Filter.builder()
                .playerId(playerId)
                .discipline(discipline)
                .seasonId(seasonService.getCurrentSeason().getId())
                .build();
        Rating rating = ratingRepository.getRatingByPlayerIdAndDisciplineAndSeasonId(filter);

        return rating == null
                ? defaultRating(playerId, discipline)
                : rating;
    }

    @Override
    public void saveResults(TgEvent event) {
        TgGame game = event.getLatestGame();
        event.getLatestGame().getPlayers()
                .stream()
                .peek(player -> saveRating(player, event.getDiscipline()))
                .forEach(player -> saveHistory(player, game));
    }

    private int winCase(TgTeam team1, TgTeam team2) {
        var diff = team1.getScore() - team2.getScore();
        var limitedDiff = Math.min(diff, Values.RATING_REFERENCE);
        var change = diff >= 0
                ? Values.BASE_RATING_CHANGE * (1 - ((float) limitedDiff / Values.RATING_REFERENCE))
                : Values.BASE_RATING_CHANGE * (1 + ((float) -limitedDiff / Values.RATING_REFERENCE));

        return Math.round(change);
    }

    private int limitAndRoundChange(float change, boolean isCalibrating) {
        var value = isCalibrating
                ? change * calibrationPenaltyMultiplier
                : change;

        if (Math.abs(value) >= getBasePool(isCalibrating)) {
            return getBasePool(isCalibrating) - 1;
        }

        return Math.max(
                Math.abs(Math.round(value)),
                1
        );
    }

    private int getBasePool(boolean isCalibrating) {
        return isCalibrating ? Values.BASE_RATING_CHANGE : Values.BASE_RATING_CHANGE * 2;
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

    private void applyCalibratingStatus(TgEventPlayer player, Discipline discipline) {
        var isCalibrated = calibrationService.isCalibrated(player.getId());
        player.setCalibrated(isCalibrated);
    }

    private void applyChanges(TgEvent event) {
        var game = event.getLatestGame();
        game.getPlayers().forEach(player -> applyCalibratingStatus(player, event.getDiscipline()));
        List.of(game.getWinnerTeam(), game.getLoserTeam()).forEach(TgTeam::applyRating);
    }

    private void saveRating(TgEventPlayer player, Discipline discipline) {
        Rating rating = getRating(player.getId(), discipline);
        rating.setScore(player.getScore());
        rating.setIsCalibrated(player.isCalibrated());

        ratingRepository.update(rating);
    }

    private void saveHistory(TgEventPlayer player, TgGame game) {
        RatingHistory ratingHistory = new RatingHistory()
                .setGameId(game.getId())
                .setPlayerId(player.getId())
                .setIscalibrated(player.isCalibrated())
                .setChange(player.getLastChange())
                .setSeasonId(seasonService.getCurrentSeason().getId())
                .setScore(player.getScore());

        ratingHistoryRepository.save(ratingHistory);
    }

}
