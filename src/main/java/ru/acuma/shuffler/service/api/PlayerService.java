package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.model.dto.TgEvent;

public interface PlayerService {

    void authenticate(TgEvent event, User user);

    void leaveLobby(TgEvent event, Long userId);

    void joinLobby(TgEvent event, User user);
}
