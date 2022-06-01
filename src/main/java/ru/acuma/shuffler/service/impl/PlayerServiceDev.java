package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.mapper.PlayerMapper;
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.entity.GameEventPlayer;
import ru.acuma.shuffler.service.PlayerService;
import ru.acuma.shuffler.service.RatingService;
import ru.acuma.k.shuffler.tables.pojos.Player;
import ru.acuma.shufflerlib.dao.PlayerDao;
import ru.acuma.shufflerlib.dao.SeasonDao;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.Comparator;
import java.util.NoSuchElementException;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class PlayerServiceDev implements PlayerService {

    private final FakeUsersFactory fakeUsersFactory;
    private final PlayerMapper playerMapper;
    private final SeasonDao seasonDao;
    private final PlayerDao playerDao;
    private final RatingService ratingService;

    @Override
    public void authenticate(GameEvent event, User user) {
        var appUser = fakeUsersFactory.getRandomUser();
        var player = registerPlayer(event.getChatId(), user.getId());
        appUser.setTelegramId(player.getId());
        var rating = ratingService.getRating(player.getId(), event.getDiscipline());
        var kickerPlayer = playerMapper.toKickerPlayer(appUser, player, rating);
        var kickerEventPlayer = playerMapper.toKickerEventPlayer(kickerPlayer);
        event.joinPlayer(kickerEventPlayer);
    }

    @Override
    public void leaveLobby(GameEvent event, User user) {
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
    public void joinLobby(GameEvent event, User user) {
        var members = event.getPlayers();
        var player = members.get(user.getId());
        int maxGames = members.values()
                .stream()
                .max(Comparator.comparingInt(GameEventPlayer::getGameCount))
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

    private Player registerPlayer(Long chatId, Long userId) {
        Player player = new Player()
                .setChatId(chatId)
                .setSeasonId(seasonDao.getCurrentSeason().getId())
                .setUserId(userId);
        player.setId(playerDao.save(player));
        ratingService.defaultRating(player, Discipline.KICKER_2VS2);
        return player;
    }
}
