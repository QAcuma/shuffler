package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.model.domain.Player;

public interface PlayerService {

    Player authenticate(User user);

}
