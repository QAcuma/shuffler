package ru.acuma.k.shuffler.dao;

import ru.acuma.k.shuffler.tables.pojos.UserInfo;

public interface UserDao {

    boolean isActive(Long telegramId);

    boolean isBlocked(Long telegramId);

    UserInfo get(Long telegramId);

    long save(UserInfo user);

    UserInfo update(Long telegramId);

    void delete(Long userId);

}
