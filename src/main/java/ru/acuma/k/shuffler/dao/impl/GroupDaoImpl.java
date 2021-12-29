package ru.acuma.k.shuffler.dao.impl;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.acuma.k.shuffler.dao.GroupDao;
import ru.acuma.k.shuffler.tables.pojos.GroupInfo;
import ru.acuma.k.shuffler.tables.records.GroupInfoRecord;

import static ru.acuma.k.shuffler.Tables.GROUP_INFO;

@Repository
@RequiredArgsConstructor
public class GroupDaoImpl implements GroupDao {

    private final DSLContext dsl;

    @Override
    public long save(GroupInfo groupInfo) {
        GroupInfoRecord record = dsl.newRecord(GROUP_INFO, groupInfo);
        return record.store();
    }

    @Override
    public boolean isActive(Long chatId) {
        return dsl.fetchExists(
                dsl.selectFrom(GROUP_INFO)
                        .where(GROUP_INFO.CHAT_ID.eq(chatId)
                                .and(GROUP_INFO.IS_BLOCKED.eq(Boolean.FALSE)
                                )));
    }

}
