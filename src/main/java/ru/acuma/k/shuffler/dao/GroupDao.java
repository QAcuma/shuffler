package ru.acuma.k.shuffler.dao;

import ru.acuma.k.shuffler.tables.pojos.GroupInfo;

public interface GroupDao {

    long save(GroupInfo groupInfo);

    boolean isActive(Long chatId);

}
