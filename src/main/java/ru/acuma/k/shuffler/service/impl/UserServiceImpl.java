package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.dao.UserDao;
import ru.acuma.k.shuffler.mapper.UserMapper;
import ru.acuma.k.shuffler.service.UserService;
import ru.acuma.k.shuffler.tables.pojos.UserInfo;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    @Override
    public UserInfo getUser(Long telegramId) {
        return userDao.get(telegramId);
    }

    private Boolean hasAccess(Long telegramId) {
        return (userDao.isActive(telegramId));
    }

    @Override
    public boolean authenticate(User user) {
        if (hasAccess(user.getId())) {
            return true;
        } else if (!userDao.isBlocked(user.getId())) {
            userDao.save(userMapper.toUserInfo(user));
            return true;
        }
        return false;
    }
}
