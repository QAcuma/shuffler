package ru.acuma.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.model.entity.GameEvent;

public interface PlayerService {

    void authenticate(GameEvent event, User user);

    void leaveLobby(GameEvent event, User user);

    void joinLobby(GameEvent event, User user);
}
