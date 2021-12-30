package ru.acuma.k.shuffler.dao.impl;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.acuma.k.shuffler.dao.PlayerDao;
import ru.acuma.k.shuffler.tables.pojos.Player;
import ru.acuma.k.shuffler.tables.records.PlayerRecord;

import static ru.acuma.k.shuffler.tables.Player.PLAYER;

@Repository
@RequiredArgsConstructor
public class PlayerDaoImpl implements PlayerDao {

    private final DSLContext dsl;

    @Override
    public boolean isPresent(Long chatId, Long userId) {
        return dsl.fetchExists(
                dsl.selectFrom(PLAYER)
                        .where(PLAYER.CHAT_ID.eq(chatId))
                        .and(PLAYER.USER_ID.eq(userId))
        );
    }

    @Override
    public Player get(Long chatId, Long userId) {
        return dsl.select()
                .from(PLAYER)
                .where(PLAYER.CHAT_ID.eq(chatId))
                .and(PLAYER.USER_ID.eq(userId))
                .fetchOneInto(Player.class);
    }

    @Override
    public long save(Player player) {
        PlayerRecord record = dsl.newRecord(PLAYER, player);
        return record.store();
    }
}
