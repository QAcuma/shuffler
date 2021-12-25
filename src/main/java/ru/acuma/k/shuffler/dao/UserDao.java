package ru.acuma.k.shuffle.dao;

import ru.acuma.k.shuffle.tables.pojos.UserInfo;

public interface UserDao {

    Boolean isActual(UserInfo user);

    Boolean isExists(Long telegramId);

    Boolean isAllowed(Long telegramId);

    UserInfo get(Long telegramId);

    Long save(UserInfo user);

    UserInfo update(Long telegramId);

    void delete(Long userId);

}
