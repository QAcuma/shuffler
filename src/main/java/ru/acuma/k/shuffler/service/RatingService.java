package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.tables.pojos.Player;

public interface RatingService {

    void update(KickerEvent event);

    void defaultRating(Player player);

}
