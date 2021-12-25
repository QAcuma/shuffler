package ru.acuma.k.shuffler.dao.impl;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import ru.acuma.k.shuffler.tables.pojos.UserInfo;
import ru.acuma.k.shuffler.tables.records.UserInfoRecord;
import ru.acuma.k.shuffler.dao.UserDao;

import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final DSLContext dsl;

    @Override
    public Boolean isActual(UserInfo userInfo) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean isExists(Long telegramId) {
        return dsl.fetchExists(
                dsl.selectFrom(ru.acuma.k.shuffler.tables.UserInfo.USER_INFO)
                        .where(ru.acuma.k.shuffler.tables.UserInfo.USER_INFO.TELEGRAM_ID.eq(telegramId)));
    }

    @Override
    public Boolean isAllowed(Long telegramId) {
        return dsl.fetchExists(
                dsl.selectFrom(ru.acuma.k.shuffler.tables.UserInfo.USER_INFO)
                        .where(ru.acuma.k.shuffler.tables.UserInfo.USER_INFO.TELEGRAM_ID.eq(telegramId)
                                .and(ru.acuma.k.shuffler.tables.UserInfo.USER_INFO.IS_BLOCKED.eq(Boolean.FALSE)
                                        .and(ru.acuma.k.shuffler.tables.UserInfo.USER_INFO.DELETED_AT.isNull()
                                        ))));
    }

    @Override
    public UserInfo get(Long telegramId) {
        Record record = dsl.select()
                .from(ru.acuma.k.shuffler.tables.UserInfo.USER_INFO)
                .where(ru.acuma.k.shuffler.tables.UserInfo.USER_INFO.TELEGRAM_ID.eq(telegramId))
                .fetchOne();
        UserInfoRecord userInfoRecord = Objects.requireNonNull(record).into(ru.acuma.k.shuffler.tables.UserInfo.USER_INFO);
        return userInfoRecord.into(UserInfo.class);
    }

    @Override
    public Long save(UserInfo userInfo) {
        UserInfoRecord record = dsl.newRecord(ru.acuma.k.shuffler.tables.UserInfo.USER_INFO, userInfo);
        return (long) record.store();
    }

    @Override
    public UserInfo update(Long userId) {
        return null;
    }

    @Override
    public void delete(Long userId) {

    }
}
