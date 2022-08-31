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
        var change = game.getWinnerTeam().getBet().getCaseWin();
        var modChange = game.isCalibrating() ? change * calibrationPenaltyMultiplier : change;

        return (int) modChange;
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

    @Override
    public void applyBet(TgTeam redTeam, TgTeam blueTeam) {
        var redBet = new TgGameBet(winCase(redTeam, blueTeam));
        var blueCaseWin = Math.abs(redBet.getCaseLose());
        var blueBet = new TgGameBet(blueCaseWin);

        redTeam.setBet(redBet);
        blueTeam.setBet(blueBet);
    }

    private int winCase(TgTeam team1, TgTeam team2) {
        var diff = team1.getScore() - team2.getScore();
        var change = diff > 0
                ? Values.BASE_RATING_CHANGE * (1 + ((double) diff / Values.RATING_REFERENCE))
                : Values.BASE_RATING_CHANGE * (1 - ((double) diff / Values.RATING_REFERENCE));

        return limitAndRoundChange(change);
    }

    @Override
    public void updateRating(TgEventPlayer player, Discipline discipline) {
        Rating rating = getRating(player.getId(), discipline);

        rating.setScore(player.getScore());
        rating.setIsCalibrated(calibrationService.isCalibrated(player.getId()));
        player.setCalibrated(rating.getIsCalibrated());

        ratingRepository.update(rating);
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
                ? winnerTeam.getBet().getCaseWin()
                : winnerTeam.getBet().getCaseLose();
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

    private void applyChanges(TgGame game, int change) {
        game.getWinnerTeam().applyRating(change);
        game.getLoserTeam().applyRating(-change);
    }

    private int limitAndRoundChange(double change) {
        int value = Math.toIntExact(Math.round(change));
        if (Math.abs(value) >= Values.BASE_RATING_CHANGE * 2) {
            return (Values.BASE_RATING_CHANGE * 2) - 1;
        }
        return Math.max(Math.abs(value), 1);
    }

}
