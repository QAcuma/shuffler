package ru.acuma.shuffler.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.mapper.PlayerMapper;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventContext;
import ru.acuma.shuffler.model.domain.TEventPlayer;
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

    @Transactional(readOnly = true)
    public TEventPlayer getEventPlayer(final User user, final TEvent event) {
        var userInfo = userService.getUser(user.getId());
        var player = getOrSignUpPlayer(event.getChatId(), user.getId());
        var rating = ratingService.getRating(player, event.getDiscipline());

        return playerMapper.toTgEventPlayer(player, userInfo, rating);
    }

    public void leaveLobby(TEvent event, Long userId) {
        switch (event.getEventStatus()) {
            case CREATED, READY -> event.leaveLobby(userId);
            default -> event.getPlayers().get(userId).getEventContext().setLeft(true);
        }
    }

    public void joinLobby(TEvent event, User user) {
        var members = event.getPlayers();
        var player = members.get(user.getId());
        int maxGames = members.values()
            .stream()
            .map(TEventPlayer::getEventContext)
            .max(Comparator.comparingInt(TEventContext::getGameCount))
            .orElseThrow(() -> new NoSuchElementException("Group member not found"))
            .getGameCount();
        if (player != null) {
            event.getPlayers().get(user.getId()).getEventContext().setLeft(false);
            player.getEventContext().setGameCount(maxGames);
        } else {
//            authenticate(event, user);
            player = members.get(user.getId());
            player.getEventContext().setGameCount(maxGames);
        }
    }

    private Player getOrSignUpPlayer(Long groupId, Long userId) {
        return playerRepository.findById(userId)
            .orElseGet(() -> signUpPlayer(groupId, userId));
    }

    private Player signUpPlayer(final Long chatId, Long userId) {
        var userInfo = userService.getUser(userId);
        var group = groupService.getGroupInfo(chatId);

        var player = Player.builder()
            .chat(group)
            .user(userInfo)
            .build();

        playerRepository.save(player);
        ratingService.defaultRating(player);
        return player;
    }

}
