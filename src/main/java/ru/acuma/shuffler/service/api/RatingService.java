package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgTeam;
import ru.acuma.shuffler.tables.pojos.Rating;
import ru.acuma.shufflerlib.model.Discipline;

public interface RatingService {

    void update(TgEvent event);

    void defaultRating(Long playerId);

    Rating defaultRating(Long playerId, Discipline discipline);

    Rating getRating(Long playerId, Discipline discipline);

    void saveResults(TgEvent event);

    void applyBet(TgTeam redTeam, TgTeam blueTeam);
}
