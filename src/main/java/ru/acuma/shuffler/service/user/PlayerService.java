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
import ru.acuma.shuffler.service.event.RatingService;
import ru.acuma.shuffler.service.telegram.GroupService;

import java.util.Map;
import java.util.Optional;

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
        var player = getOrSignUpPlayer(event.getChatId(), user.getId());
        var userInfo = userService.getUser(user.getId());
        var rating = ratingService.getRating(player, event.getDiscipline());

        return playerMapper.toTgEventPlayer(player, userInfo, rating);
    }

    public void leaveLobby(User user, TEvent event) {
        switch (event.getEventStatus()) {
            case CREATED, READY -> event.leaveLobby(user.getId());
            default -> event.getPlayers().get(user.getId()).getEventContext().setLeft(true);
        }
    }

    @Transactional(readOnly = true)
    public void join(User user, TEvent event) {
        var members = event.getPlayers();
        int maxGames = getMaxGames(members);
        Optional.ofNullable(members.get(user.getId()))
            .ifPresentOrElse(
                player -> event.joinPlayer(player)
                    .getEventContext()
                    .setGameCount(maxGames),
                () -> event.joinPlayer(getEventPlayer(user, event))
                    .getEventContext()
                    .setGameCount(maxGames)
            );
    }

    private Integer getMaxGames(final Map<Long, TEventPlayer> members) {
        return members.values()
            .stream()
            .map(TEventPlayer::getEventContext)
            .mapToInt(TEventContext::getGameCount)
            .max()
            .orElse(0);
    }

    private Player getOrSignUpPlayer(final Long groupId, final Long userId) {
        return playerRepository.findById(userId)
            .orElseGet(() -> signUpPlayer(groupId, userId));
    }

    private Player signUpPlayer(final Long chatId, final Long userId) {
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
