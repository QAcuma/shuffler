package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.tables.pojos.UserInfo;

public interface UserService {

    UserInfo getUser(Long telegramId);

    boolean authenticate(User user);
}
