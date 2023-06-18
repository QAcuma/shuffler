package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.RatingMapper;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.domain.TGameBet;
import ru.acuma.shuffler.model.domain.TRating;
import ru.acuma.shuffler.model.domain.TTeam;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Rating;
import ru.acuma.shuffler.repository.RatingRepository;
import ru.acuma.shuffler.service.season.SeasonService;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final SeasonService seasonService;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    public void applyBet(TTeam redTeam, TTeam blueTeam) {
        var redWinCase = caseVictory(redTeam, blueTeam);
        var limitedRedWinCase = limitAndRoundChange(redWinCase);
        var blueWinCase = Constants.BASE_GAME_RATING_POOL - limitedRedWinCase;

        var redLoseCase = -1 * blueWinCase;
        var blueLoseCase = -1 * limitedRedWinCase;

        var redBet = TGameBet.builder().caseWin(limitedRedWinCase).caseLose(redLoseCase).build();
        var blueBet = TGameBet.builder().caseWin(blueWinCase).caseLose(blueLoseCase).build();

        redTeam.setBaseBet(redBet);
        blueTeam.setBaseBet(blueBet);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public TRating getRatingOrDefault(final Player player, final Discipline discipline) {
        return ratingRepository.findBySeasonIdAndPlayerAndDiscipline(
                seasonService.getSeasonId(),
                player,
                discipline)
            .map(ratingMapper::toRating)
            .orElseGet(() -> ratingMapper.defaultRating(player, discipline));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Rating findRating(final Long id) {
        return ratingRepository.findById(id)
            .orElseThrow(() -> new DataException(ExceptionCause.RATING_NOT_FOUND, id));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateRating(final TRating ratingContext) {
        var rating = findRating(ratingContext.getId());

        ratingMapper.update(rating, ratingContext);
    }

    private int caseVictory(final TTeam team1, final TTeam team2) {
        var diff = team1.getScore() - team2.getScore();
        var limitedDiff = Math.min(diff, Constants.RATING_REFERENCE);
        var change = diff >= 0
                     ? Constants.BASE_RATING_CHANGE * (1 - ((float) limitedDiff / Constants.RATING_REFERENCE))
                     : Constants.BASE_RATING_CHANGE * (1 + ((float) -limitedDiff / Constants.RATING_REFERENCE));

        return Math.round(change);
    }

    private int limitAndRoundChange(float change) {
        if (Math.abs(change) >= Constants.BASE_GAME_RATING_POOL) {
            return Constants.BASE_GAME_RATING_POOL - 1;
        }

        return Math.max(Math.abs(Math.round(change)), 1);
    }
}
