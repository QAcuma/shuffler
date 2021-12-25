package ru.acuma.k.shuffle.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffle.tables.pojos.UserInfo;
import ru.acuma.k.shuffle.dao.UserDao;
import ru.acuma.k.shuffle.mapper.UserMapper;
import ru.acuma.k.shuffle.service.UserService;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    @Override
    public UserInfo getUser(Long telegramId) {
        return userDao.get(telegramId);
    }

    @Override
    public Boolean isStored(Long telegramId) {
        return userDao.isExists(telegramId);
    }

    @Override
    public Boolean hasAccess(Long telegramId) {
        return (isStored(telegramId) && userDao.isAllowed(telegramId));
    }

    @Override
    public Boolean validate(User user) {
        if (hasAccess(user.getId())) {
            return Boolean.TRUE;
        }
        if (!isStored(user.getId())) {
            UserInfo userInfo = userMapper.toUserInfo(user);
            userInfo.setCreatedAt(OffsetDateTime.now());
            userDao.save(userInfo);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
