package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.dao.PlayerDao;
import ru.acuma.k.shuffler.mapper.PlayerMapper;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.service.PlayerService;
import ru.acuma.k.shuffler.service.UserService;
import ru.acuma.k.shuffler.tables.pojos.Player;

import static ru.acuma.k.shuffler.model.enums.Values.DEFAULT_RATING;

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
