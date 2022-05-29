package ru.acuma.k.shuffler.service.impl;

import lombok.Getter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.acuma.k.shuffler.tables.pojos.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("dev")
@Getter
public class FakeUsersFactory {

    private Map<Long, UserInfo> fakePlayers = new ConcurrentHashMap<>();
    private List<Long> playerIds = new ArrayList<>(List.of(1L, 2L, 3L, 4L, 5L));

    public UserInfo getRandomUser() {
        Long id = playerIds.stream()
                .findFirst()
                .orElseThrow(() -> new IndexOutOfBoundsException("Limit of players"));
        playerIds.removeIf(id::equals);
        fakePlayers.put(id, randomUser(id));
        return fakePlayers.get(id);
    }

    public void returnUserToPull(Long userId) {
        playerIds.add(userId);
        fakePlayers.remove(userId);
    }

    private UserInfo randomUser(Long telegramId) {
        UserInfo user = new UserInfo();
        user.setTelegramId(telegramId);
        user.setIsBot(Boolean.FALSE);
        user.setUserName("user_" + telegramId);
        user.setFirstName("first_" + telegramId);

        return user;
    }


}
