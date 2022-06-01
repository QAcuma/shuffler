package ru.acuma.shuffler.service;

import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.entity.GameEventPlayer;
import ru.acuma.k.shuffler.tables.pojos.Player;
import ru.acuma.k.shuffler.tables.pojos.Rating;
import ru.acuma.shufflerlib.model.Discipline;

public interface RatingService {

    void update(GameEvent event);

    void defaultRating(Player player, Discipline discipline);

    Rating getRating(Long id, Discipline discipline);

    void updateRating(GameEventPlayer player, Discipline discipline);

    void updatePlayersRating(GameEvent event);
}
