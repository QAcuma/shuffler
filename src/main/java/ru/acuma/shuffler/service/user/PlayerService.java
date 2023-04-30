package ru.acuma.shuffler.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.PlayerMapper;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.repository.PlayerRepository;
import ru.acuma.shuffler.service.event.RatingService;
import ru.acuma.shuffler.service.telegram.GroupService;

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
        var player = getPlayer(event.getChatId(), user.getId());
        var userInfo = userService.getUser(user.getId());
        var rating = ratingService.getRating(player, event.getDiscipline());

        return playerMapper.toTgEventPlayer(player, userInfo, rating);
    }

    public void leaveLobby(final User user, final TEvent event) {
        event.leaveLobby(user.getId());
    }

    public void leaveEvent(final User user, final TEvent event) {
        event.getPlayers().get(user.getId())
            .getEventContext()
            .setLeft(true);
    }

    @Transactional(readOnly = true)
    public void join(User user, TEvent event) {
        Optional.ofNullable(event.getPlayers().get(user.getId()))
            .ifPresentOrElse(
                event::joinPlayer,
                () -> {
                    var eventPlayer = getEventPlayer(user, event);
                    event.joinPlayer(eventPlayer);
                }
            );
    }

    private Player getPlayer(final Long chatId, final Long userId) {
        return playerRepository.findById(userId)
            .orElseThrow(() -> new DataException(ExceptionCause.PLAYER_NOT_FOUND, userId, chatId));
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
