package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.RatingMapper;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.domain.TGameBet;
import ru.acuma.shuffler.model.domain.TRating;
import ru.acuma.shuffler.model.domain.TTeam;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.repository.RatingRepository;
import ru.acuma.shuffler.service.season.SeasonService;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final SeasonService seasonService;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    @Value("${rating.calibration.game-penalty}")
    private float calibrationPenaltyMultiplier;

    public void applyBet(TTeam redTeam, TTeam blueTeam) {
        boolean isCalibrating = redTeam.containsCalibrating() || blueTeam.containsCalibrating();

        var redWinCase = winCase(redTeam, blueTeam);
        var limitedRedWinCase = limitAndRoundChange(redWinCase, isCalibrating);
        var blueWinCase = getBasePool(isCalibrating) - limitedRedWinCase;

        var redLoseCase = -1 * blueWinCase;
        var blueLoseCase = -1 * limitedRedWinCase;

        var redBet = TGameBet.builder().caseWin(limitedRedWinCase).caseLose(redLoseCase).build();
        var blueBet = TGameBet.builder().caseWin(blueWinCase).caseLose(blueLoseCase).build();

        redTeam.setBet(redBet);
        blueTeam.setBet(blueBet);
    }

    public TRating getRatingOrDefault(final Player player, final Discipline discipline) {
        return ratingRepository.findBySeasonAndPlayerAndDiscipline(
                seasonService.getCurrentSeason(),
                player,
                discipline)
            .map(ratingMapper::toRating)
            .orElseGet(() -> ratingMapper.defaultRating(player, discipline));
    }

    private int winCase(final TTeam team1, final TTeam team2) {
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

        return Math.max(Math.abs(Math.round(value)), 1);
    }

    private int getBasePool(boolean isCalibrating) {
        return isCalibrating ? Constants.BASE_RATING_CHANGE : Constants.BASE_RATING_CHANGE * 2;
    }

}
