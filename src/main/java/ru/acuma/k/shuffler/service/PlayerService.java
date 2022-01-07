package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;

public interface PlayerService {

    void authenticate(KickerEvent event, User user);

    void updatePlayersRating(KickerEvent event);
}
