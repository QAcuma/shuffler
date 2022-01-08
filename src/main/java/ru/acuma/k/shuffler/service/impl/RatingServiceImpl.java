package ru.acuma.k.shuffler.service.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.service.RatingService;

import javax.management.InstanceNotFoundException;

import static ru.acuma.k.shuffler.model.enums.Values.BASE_RATING_CHANGE;
import static ru.acuma.k.shuffler.model.enums.Values.RATING_REFERENCE;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

    @SneakyThrows
    @Override
    public void update(KickerGame game) {
        if (game.getWinnerTeam() == null) {
            throw new InstanceNotFoundException("Отсутствует победившая команда");
        }
        double diff = game.getWinnerTeam().getRating() - game.getLoserTeam().getRating();

        if (diff >= 0) {
            strongestWon(game, diff);
        } else {
            weakestWon(game, -diff);
        }
    }

    private void strongestWon(KickerGame game, double diff) {
        double change = BASE_RATING_CHANGE * (1 - (diff / RATING_REFERENCE));
        long value = correcting(change);
        game.getWinnerTeam().setRatingChange(value);
        game.getLoserTeam().setRatingChange(-value);
        game.getWinnerTeam().getPlayers().forEach(player -> player.plusRating(value));
        game.getLoserTeam().getPlayers().forEach(player -> player.minusRating(value));
    }

    private void weakestWon(KickerGame game, double diff) {
        double change = BASE_RATING_CHANGE * (1 + (diff / RATING_REFERENCE));
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
