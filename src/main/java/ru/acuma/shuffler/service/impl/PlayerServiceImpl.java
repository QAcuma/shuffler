package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.mapper.PlayerMapper;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.service.PlayerService;
import ru.acuma.shuffler.service.RatingService;
import ru.acuma.shuffler.service.SeasonService;
import ru.acuma.shuffler.service.UserService;
import ru.acuma.shuffler.tables.pojos.Player;
import ru.acuma.shufflerlib.repository.PlayerRepository;

import java.util.Comparator;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@SuppressWarnings("Duplicates")
@ConditionalOnSingleCandidate(PlayerService.class)
public class PlayerServiceImpl implements PlayerService {

    private final UserService userService;
    private final PlayerMapper playerMapper;
    private final SeasonService seasonService;
    private final PlayerRepository playerRepository;
    private final RatingService ratingService;

    @Override
    public void authenticate(TgEvent event, User user) {
        var appUser = userService.getUser(user.getId());
        var player = getPlayer(event.getChatId(), user.getId());
        var rating = ratingService.getRating(player.getId(), event.getDiscipline());
        var tgPlayer = playerMapper.toTgPlayer(appUser, player, rating);
        var kickerEventPlayer = playerMapper.toTgEventPlayer(tgPlayer);
        event.joinPlayer(kickerEventPlayer);
    }

    @Override
    public void leaveLobby(TgEvent event, User user) {
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
    public void joinLobby(TgEvent event, User user) {
        var members = event.getPlayers();
        var player = members.get(user.getId());
        int maxGames = members.values()
                .stream()
                .max(Comparator.comparingInt(TgEventPlayer::getGameCount))
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

    private Player getPlayer(Long chatId, Long userId) {
        if (playerRepository.isPresent(chatId, userId)) {
            return playerRepository.get(chatId, userId);
        }
        registerPlayer(chatId, userId);
        return getPlayer(chatId, userId);
    }

    private void registerPlayer(Long chatId, Long userId) {
        Player player = new Player()
                .setChatId(chatId)
                .setSeasonId(seasonService.getCurrentSeason().getId())
                .setUserId(userId);
        player.setId(playerRepository.save(player));
        ratingService.defaultRating(player.getId());
    }

}
