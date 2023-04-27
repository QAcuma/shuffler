package ru.acuma.shuffler.service.game;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.domain.TgEventPlayer;
import ru.acuma.shuffler.model.domain.TgGame;
import ru.acuma.shuffler.model.domain.TgGameBet;
import ru.acuma.shuffler.model.domain.TgTeam;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Rating;
import ru.acuma.shuffler.model.entity.RatingHistory;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.repository.RatingHistoryRepository;
import ru.acuma.shuffler.repository.RatingRepository;
import ru.acuma.shuffler.service.season.SeasonService;
import ru.acuma.shufflerlib.model.Discipline;

import javax.management.InstanceNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final SeasonService seasonService;
    private final RatingHistoryRepository ratingHistoryRepository;
    private final RatingRepository ratingRepository;
    private final CalibrationService calibrationService;

    @Value("${rating.calibration.game-penalty}")
    private float calibrationPenaltyMultiplier;

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

    @SneakyThrows
    @Transactional
    public void update(TgEvent event) {
        var game = event.getLatestGame();
        Optional.ofNullable(game.getWinnerTeam()).orElseThrow(() -> new InstanceNotFoundException("Отсутствует победившая команда"));
        applyChanges(event);
        saveResults(event);
    }

    public void defaultRating(Player player) {
        Arrays.stream(Discipline.values()).forEach(discipline -> defaultRating(player, discipline));
    }

    public Rating defaultRating(Player player, Discipline discipline) {
        return newDefaultRating(player, discipline);
    }

    public Rating getRating(Player player, Discipline discipline) {
        return ratingRepository.findBySeasonAndPlayerIdAndDiscipline(
                seasonService.getCurrentSeason(),
                player,
                discipline)
            .orElse(defaultRating(player, discipline));
    }

    public void saveResults(TgEvent event) {
        TgGame game = event.getLatestGame();
        event.getLatestGame().getPlayers()
            .stream()
            .peek(player -> saveRating(player, event.getDiscipline()))
            .forEach(player -> saveHistory(player, game, event.getDiscipline()));
    }

    private int winCase(TgTeam team1, TgTeam team2) {
        var diff = team1.getScore() - team2.getScore();
        var limitedDiff = Math.min(diff, Constants.RATING_REFERENCE);
        var change = diff >= 0
                     ? Constants.BASE_RATING_CHANGE * (1 - ((float) limitedDiff / Constants.RATING_REFERENCE))
                     : Constants.BASE_RATING_CHANGE * (1 + ((float) -limitedDiff / Constants.RATING_REFERENCE));

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
        return isCalibrating ? Constants.BASE_RATING_CHANGE : Constants.BASE_RATING_CHANGE * 2;
    }

    private Rating newDefaultRating(Player player, Discipline discipline) {
        var rating = Rating.builder()
            .discipline(discipline)
            .season(seasonService.getCurrentSeason())
            .player(player)
            .score(Constants.BASE_RATING)
            .isCalibrated(false)
            .build();

        return ratingRepository.save(rating);
    }

    private void applyCalibratingStatus(TgEventPlayer player, Discipline discipline) {
        var isCalibrated = calibrationService.isCalibrated(player.getId(), discipline);
        player.getRatingContext().setCalibrated(isCalibrated);
    }

    private void applyChanges(TgEvent event) {
        var game = event.getLatestGame();
        game.getPlayers().forEach(player -> applyCalibratingStatus(player, event.getDiscipline()));
        List.of(game.getWinnerTeam(), game.getLoserTeam()).forEach(TgTeam::applyRating);
    }

    private void saveRating(TgEventPlayer player, Discipline discipline) {
        Rating rating = getRating(null, discipline);
        rating.setScore(player.getRatingContext().getScore());
        rating.setIsCalibrated(player.isCalibrated());

        ratingRepository.save(rating);
    }

    private void saveHistory(TgEventPlayer player, TgGame game, Discipline discipline) {
        RatingHistory ratingHistory = RatingHistory.builder()
            .game(null)
            .player(null)
            .isCalibrated(player.isCalibrated())
            .change(player.getRatingContext().getLastScoreChange())
            .discipline(discipline)
            .season(seasonService.getCurrentSeason())
            .score(player.getRatingContext().getScore())
            .build();

        ratingHistoryRepository.save(ratingHistory);
    }

}
