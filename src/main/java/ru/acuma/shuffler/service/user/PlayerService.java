package ru.acuma.shuffler.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.mapper.PlayerMapper;
import ru.acuma.shuffler.model.dto.TgEvent;
import ru.acuma.shuffler.model.dto.TgEventPlayer;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.repository.PlayerRepository;
import ru.acuma.shuffler.service.game.RatingService;
import ru.acuma.shuffler.service.telegram.GroupService;

import java.util.Comparator;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final RatingService ratingService;
    private final UserService userService;
    private final GroupService groupService;
    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;

    @Transactional
    public TgEventPlayer getEventPlayer(User user, TgEvent event) {
        var userInfo = userService.getUser(user.getId());
        var player = getOrSignUpPlayer(event.getChatId(), user.getId());
        var rating = ratingService.getRating(player, event.getDiscipline());
        var tgEventPlayer = playerMapper.toTgEventPlayer(userInfo, player, rating);

        return tgEventPlayer;
    }

    public void leaveLobby(TgEvent event, Long userId) {
        switch (event.getEventState()) {
            case CREATED, READY -> event.leaveLobby(userId);
            default -> event.getPlayers().get(userId).setLeft(true);
        }
    }

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
//            authenticate(event, user);
            player = members.get(user.getId());
            player.setGameCount(maxGames);
        }
    }

    private Player getOrSignUpPlayer(Long groupId, Long userId) {
        return playerRepository.findById(userId)
            .orElseGet(() -> signUpPlayer(groupId, userId));
    }

    private Player signUpPlayer(Long chatId, Long userId) {
        var userInfo = userService.getUser(userId);
        var group = groupService.getGroupInfo(chatId);

        var player = Player.builder()
            .chat(group)
            .user(userInfo)
            .build();

        playerRepository.save(player);
//        ratingService.defaultRating(player);
        return player;
    }

}
