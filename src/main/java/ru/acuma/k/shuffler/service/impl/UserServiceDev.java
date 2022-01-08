package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.dao.UserDao;
import ru.acuma.k.shuffler.mapper.UserMapper;
import ru.acuma.k.shuffler.service.UserService;
import ru.acuma.k.shuffler.tables.pojos.UserInfo;

@Profile("dev")
@Service
@RequiredArgsConstructor
public class UserServiceDev implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    @Override
    public UserInfo getUser(Long telegramId) {
        return userDao.get(telegramId);
    }

    private Boolean hasAccess(Long telegramId) {
        return true;
    }

    @Override
    public boolean authenticate(User user) {
        return true;
    }
}
