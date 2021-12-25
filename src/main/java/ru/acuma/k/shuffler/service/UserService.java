package ru.acuma.k.shuffle.service;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffle.tables.pojos.UserInfo;

public interface UserService {

    UserInfo getUser(Long telegramId);

    Boolean isStored(Long telegramId);

    Boolean hasAccess(Long telegramId);

    Boolean validate(User user);
}
