package ru.acuma.trgb.service;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.trgb.tables.pojos.UserInfo;

public interface UserService {

    UserInfo getUser(Long telegramId);

    Boolean isStored(Long telegramId);

    Boolean hasAccess(Long telegramId);

    Boolean validate(User user);
}
