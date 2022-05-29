package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.tables.pojos.Player;
import ru.acuma.k.shuffler.tables.pojos.Rating;
import ru.acuma.shufflerlib.model.Discipline;

public interface RatingService {

    void update(KickerEvent event);

    void defaultRating(Player player, Discipline discipline);

    Rating getRating(Long id, Discipline discipline);

    void updateRating(KickerEventPlayer player, Discipline discipline);

    void updatePlayersRating(KickerEvent event);
}
