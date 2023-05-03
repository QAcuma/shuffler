package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.PlayerMapper;
import ru.acuma.shuffler.model.constant.AuthStatus;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.wrapper.SearchPlayerParams;
import ru.acuma.shuffler.repository.PlayerRepository;
import ru.acuma.shuffler.service.event.RatingService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService implements Authenticatable<SearchPlayerParams> {
    private final RatingService ratingService;
    private final UserService userService;
    private final ChatService chatService;
    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    public TEventPlayer getEventPlayer(final User user, final TEvent event) {
        var userInfo = userService.getUser(user.getId());
        var player = getPlayer(event.getChatId(), user.getId());
        var rating = ratingService.getRating(player, event.getDiscipline());

        return playerMapper.toTgEventPlayer(player, userInfo, rating);
    }

    private Player getPlayer(final Long chatId, final Long userId) {
        return playerRepository.findByUserIdAndChatId(userId, chatId)
            .orElseThrow(() -> new DataException(ExceptionCause.PLAYER_NOT_FOUND, userId, chatId));
    }

    public void leaveLobby(final User user, final TEvent event) {
        event.leaveLobby(user.getId());
    }

    public void leaveEvent(final User user, final TEvent event) {
        event.leaveEvent(user.getId());
    }

    @Transactional(readOnly = true)
    public void join(final User user, final TEvent event) {
        Optional.ofNullable(event.getPlayers().get(user.getId()))
            .ifPresentOrElse(
                eventPlayer -> eventPlayer.getEventContext().setLeft(Boolean.FALSE),
                () -> {
                    var eventPlayer = getEventPlayer(user, event);
                    event.joinPlayer(eventPlayer);
                }
            );
    }

    @Transactional
    public void signUp(final Long chatId, final Long userId) {
        var user = userService.getUser(userId);
        var group = chatService.getGroupInfo(chatId);
        var player = playerMapper.defaultPlayer(user, group);

        playerRepository.save(player);
    }

    @Override
    public AuthStatus authenticate(SearchPlayerParams findParams) {
        return playerRepository.findByUserIdAndChatId(findParams.getUserId(), findParams.getChatId())
            .map(player -> AuthStatus.SUCCESS)
            .orElse(AuthStatus.UNREGISTERED);
    }
}
