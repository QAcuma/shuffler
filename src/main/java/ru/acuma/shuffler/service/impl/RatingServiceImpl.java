package ru.acuma.shuffler.service.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TgGame;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.service.RatingService;
import ru.acuma.shuffler.service.SeasonService;
import ru.acuma.shuffler.tables.pojos.Rating;
import ru.acuma.shufflerlib.model.Discipline;
import ru.acuma.shufflerlib.model.Filter;
import ru.acuma.shufflerlib.repository.RatingRepository;

import javax.management.InstanceNotFoundException;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final SeasonService seasonService;
    private final RatingRepository ratingRepository;

    @SneakyThrows
    @Override
    public void update(TgEvent event) {
        var game = event.getCurrentGame();
        if (game.getWinnerTeam() == null) {
            throw new InstanceNotFoundException("Отсутствует победившая команда");
        }
        double diff = game.getWinnerTeam().getRating() - game.getLoserTeam().getRating();

        if (diff >= 0) {
            strongestWon(game, diff);
        } else {
            weakestWon(game, -diff);
        }
        updatePlayersRating(event);
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

        return rating == null ? defaultRating(playerId, discipline) : rating;
    }

    @Override
    public void updatePlayersRating(TgEvent event) {
        event.getCurrentGame().getPlayers().forEach(player -> updateRating(player, event.getDiscipline()));
    }

    @Override
    public void updateRating(TgEventPlayer player, Discipline discipline) {
        Rating rating = getRating(player.getId(), discipline);
        rating.setRating(player.getRating());
        ratingRepository.update(rating);
    }

    private Rating newDefaultRating(Long playerId, Discipline discipline) {
        Rating rating = new Rating()
                .setDiscipline(discipline.name())
                .setSeasonId(seasonService.getCurrentSeason().getId())
                .setPlayerId(playerId)
                .setRating(Values.DEFAULT_RATING);
        return rating.setId(ratingRepository.save(rating));
    }

    private void weakestWon(TgGame tgGame, double diff) {
        double change = Values.BASE_RATING_CHANGE * (1 + (diff / Values.RATING_REFERENCE));
        processChanges(tgGame, change);
    }

    private void strongestWon(TgGame tgGame, double diff) {
        double change = Values.BASE_RATING_CHANGE * (1 - (diff / Values.RATING_REFERENCE));
        processChanges(tgGame, change);
    }

    private void processChanges(TgGame tgGame, double change) {
        long value = correcting(change);
        tgGame.getWinnerTeam().setRatingChange(value);
        tgGame.getLoserTeam().setRatingChange(-value);
        tgGame.getWinnerTeam().getPlayers().forEach(player -> player.plusRating(value));
        tgGame.getLoserTeam().getPlayers().forEach(player -> player.minusRating(value));
    }

    private long correcting(double change) {
        long value = Math.round(change);
        if (value > 49) {
            return 49;
        }
        if (value < 1) {
            return 1;
        }
        return value;
    }

}
