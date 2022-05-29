package ru.acuma.k.shuffler.service.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.service.PlayerService;
import ru.acuma.k.shuffler.service.RatingService;
import ru.acuma.k.shuffler.tables.pojos.Player;
import ru.acuma.k.shuffler.tables.pojos.Rating;
import ru.acuma.shufflerlib.dao.RatingDao;
import ru.acuma.shufflerlib.dao.SeasonDao;
import ru.acuma.shufflerlib.model.Discipline;

import javax.management.InstanceNotFoundException;

import static ru.acuma.k.shuffler.model.enums.Values.BASE_RATING_CHANGE;
import static ru.acuma.k.shuffler.model.enums.Values.DEFAULT_RATING;
import static ru.acuma.k.shuffler.model.enums.Values.RATING_REFERENCE;
import static ru.acuma.shufflerlib.model.Discipline.KICKER_2VS2;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final SeasonDao seasonDao;
    private final RatingDao ratingDao;

    @SneakyThrows
    @Override
    public void update(KickerEvent event) {
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
    public void defaultRating(Player player, Discipline discipline) {
        Rating rating = new Rating()
                .setDiscipline(KICKER_2VS2.name())
                .setSeasonId(seasonDao.getCurrentSeason().getId())
                .setPlayerId(player.getId())
                .setRating(DEFAULT_RATING);
        ratingDao.save(rating);
    }

    @Override
    public Rating getRating(Long id, Discipline discipline) {
        return ratingDao.getRating(id, discipline);
    }

    @Override
    public void updatePlayersRating(KickerEvent event) {
        event.getCurrentGame().getPlayers().forEach(player -> updateRating(player, event.getDiscipline()));
    }

    @Override
    public void updateRating(KickerEventPlayer player, Discipline discipline) {
        Rating rating = getRating(player.getId(), discipline);
        rating.setRating(player.getRating());
        ratingDao.update(rating);
    }

    private void weakestWon(KickerGame game, double diff) {
        double change = BASE_RATING_CHANGE * (1 + (diff / RATING_REFERENCE));
        processChanges(game, change);
    }

    private void strongestWon(KickerGame game, double diff) {
        double change = BASE_RATING_CHANGE * (1 - (diff / RATING_REFERENCE));
        processChanges(game, change);
    }

    private void processChanges(KickerGame game, double change) {
        long value = correcting(change);
        game.getWinnerTeam().setRatingChange(value);
        game.getLoserTeam().setRatingChange(-value);
        game.getWinnerTeam().getPlayers().forEach(player -> player.plusRating(value));
        game.getLoserTeam().getPlayers().forEach(player -> player.minusRating(value));
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
