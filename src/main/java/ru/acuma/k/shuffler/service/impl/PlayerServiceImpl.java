package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.mapper.PlayerMapper;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.service.PlayerService;
import ru.acuma.k.shuffler.service.UserService;
import ru.acuma.k.shuffler.tables.pojos.Player;
import ru.acuma.k.shuffler.tables.pojos.Rating;
import ru.acuma.shufflerlib.dao.PlayerDao;
import ru.acuma.shufflerlib.dao.RatingDao;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.Comparator;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final UserService userService;
    private final PlayerMapper playerMapper;
    private final PlayerDao playerDao;
    private final RatingDao ratingDao;

    @Override
    public void authenticate(KickerEvent event, User user) {
        var appUser = userService.getUser(user.getId());
        var player = getPlayer(event.getChatId(), user.getId());
        var rating = ratingDao.getRating(user.getId(), event.getDiscipline());
        var kickerPlayer = playerMapper.toKickerPlayer(appUser, player, rating);
        var kickerEventPlayer = playerMapper.toKickerEventPlayer(kickerPlayer);
        event.joinPlayer(kickerEventPlayer);
    }

    @Override
    public void updatePlayersRating(KickerEvent event) {
        event.getCurrentGame().getPlayers().forEach(this::updateRating);
    }

    @Override
    public void leaveLobby(KickerEvent event, User user) {
        switch (event.getEventState()) {
            case CREATED:
            case READY:
                event.leaveLobby(user.getId());
                break;
            default:
                event.getPlayers().get(user.getId()).setLeft(true);
        }
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
        Rating rating =  ratingDao.getRating(player.getId(), Discipline.KICKER_2VS2);
        rating.setRating(player.getRating());
        ratingDao.save(rating);
    }

    private Player getPlayer(Long chatId, Long userId) {
        if (playerDao.isPresent(chatId, userId)) {
            return playerDao.get(chatId, userId);
        }
        registerPlayer(chatId, userId);
        return getPlayer(chatId, userId);
    }

    private void registerPlayer(Long chatId, Long userId) {
        Player player = new Player()
                .setChatId(chatId)
                .setUserId(userId);
        playerDao.save(player);
    }

}
