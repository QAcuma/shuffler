package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.dao.PlayerDao;
import ru.acuma.k.shuffler.mapper.PlayerMapper;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.service.PlayerService;
import ru.acuma.k.shuffler.service.UserService;
import ru.acuma.k.shuffler.tables.pojos.Player;

import java.util.concurrent.ThreadLocalRandom;

@Profile("dev")
@Service
@RequiredArgsConstructor
public class PlayerServiceDev implements PlayerService {

    private final UserService userService;
    private final PlayerMapper playerMapper;
    private final PlayerDao playerDao;

    @Override
    public void authenticate(KickerEvent event, User user) {
        var appUser = userService.getUser(user.getId());
        appUser.setTelegramId(ThreadLocalRandom.current().nextLong(100000000));
        appUser.setFirstName("Player");
        appUser.setLastName("" + ThreadLocalRandom.current().nextLong(100));
        var player = getPlayer(event.getChatId(), user.getId());
        var kickerPlayer = playerMapper.toKickerPlayer(appUser, player);
        kickerPlayer.setRating(ThreadLocalRandom.current().nextLong(650, 1350));
        var kickerEventPlayer = playerMapper.toKickerEventPlayer(kickerPlayer);
        event.joinPlayer(kickerEventPlayer);
    }

    @Override
    public void updatePlayersRating(KickerEvent event) {
        event.getCurrentGame().getPlayers().forEach(this::updateRating);
    }

    @Override
    public boolean leaveLobby(KickerEvent event, User from) {
        return false;
    }

    @Override
    public void joinLobby(KickerEvent event, User from) {

    }

    private void updateRating(KickerEventPlayer player) {
        playerDao.updateRating(playerMapper.toPlayer(player));
    }

    private Player getPlayer(Long chatId, Long userId) {
        if (playerDao.isPresent(chatId, userId)) {
            return playerDao.get(chatId, userId);
        }
        registerPlayer(chatId, userId);
        return getPlayer(chatId, userId);
    }

    private void registerPlayer(Long chatId, Long userId) {
        Player player = new Player();
        int rating = ThreadLocalRandom.current().nextInt(650, 1350);
        player.setChatId(chatId)
                .setUserId(ThreadLocalRandom.current().nextLong(100000))
                .setRating(rating);
    }

}
