package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.model.entity.TgEvent;

public interface PlayerService {

    void authenticate(TgEvent event, User user);

    void leaveLobby(TgEvent event, User user);

    void joinLobby(TgEvent event, User user);
}
