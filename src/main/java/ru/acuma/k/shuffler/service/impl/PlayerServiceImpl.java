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

import java.util.Comparator;
import java.util.NoSuchElementException;

import static ru.acuma.k.shuffler.model.enums.Values.DEFAULT_RATING;

@Profile("!dev")
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final UserService userService;
    private final PlayerMapper playerMapper;
    private final PlayerDao playerDao;

    @Override
    public void authenticate(KickerEvent event, User user) {
        var appUser = userService.getUser(user.getId());
        var player = getPlayer(event.getChatId(), user.getId());
        var kickerPlayer = playerMapper.toKickerPlayer(appUser, player);
        var kickerEventPlayer = playerMapper.toKickerEventPlayer(kickerPlayer);
        event.joinPlayer(kickerEventPlayer);
    }

    @Override
    public void updatePlayersRating(KickerEvent event) {
        event.getCurrentGame().getPlayers().forEach(this::updateRating);
    }

    @Override
    public boolean leaveLobby(KickerEvent event, User user) {
        boolean broken = false;
        switch (event.getEventState()) {
            case CREATED:
            case READY:
                event.leaveLobby(user.getId());
                break;
            case PLAYING:
                event.getPlayers().get(user.getId()).setLeft(true);
                broken = event.getCurrentGame().getPlayers()
                        .stream()
                        .anyMatch(KickerEventPlayer::isLeft);
                break;
        }
        return broken;
    }

    @Override
    public void joinLobby(KickerEvent event, User user) {
        var members = event.getPlayers();
        var player = members.get(user.getId());
        int maxGames = members.values()
                .stream()
                .max(Comparator.comparingInt(KickerEventPlayer::getGameCount))
                .orElseThrow(() -> new NoSuchElementException("Group member not found"))
                .getGameCount();
        if (player != null) {
            event.getPlayers().get(user.getId()).setLeft(false);
            player.setGameCount(maxGames);
        } else {
            authenticate(event, user);
            player = members.get(user.getId());
            player.setGameCount(maxGames);
        }
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
        player.setChatId(chatId)
                .setUserId(userId)
                .setRating(DEFAULT_RATING);
        playerDao.save(player);
    }

}
