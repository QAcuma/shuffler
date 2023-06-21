package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.PlayerMapper;
import ru.acuma.shuffler.model.constant.AuthStatus;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.TeamPlayer;
import ru.acuma.shuffler.model.entity.UserInfo;
import ru.acuma.shuffler.model.wrapper.SearchPlayerParams;
import ru.acuma.shuffler.repository.PlayerRepository;
import ru.acuma.shuffler.repository.ReferenceService;
import ru.acuma.shuffler.service.telegram.Authenticatable;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService implements Authenticatable<SearchPlayerParams> {

    private final RatingService ratingService;
    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;
    private final ReferenceService referenceService;
    @Lazy
    private final PlayerService self;

    @Transactional(readOnly = true)
    public TEventPlayer getEventPlayer(final User user, final TEvent event) {
        var player = self.find(event.getChatId(), user.getId());
        var rating = ratingService.getRatingOrDefault(player, event.getDiscipline());

        return playerMapper.toTgEventPlayer(player, rating);
    }

    public void leaveLobby(final Long userId, final TEvent event) {
        event.leaveLobby(userId);
    }

    public void leaveEvent(final Long userId, final TEvent event) {
        event.leaveEvent(userId);
    }

    @Transactional(readOnly = true)
    public TEventPlayer join(final User user, final TEvent event) {
        return Optional.ofNullable(event.getPlayers().get(user.getId()))
            .map(eventPlayer -> {
                eventPlayer.getEventContext().setLeft(Boolean.FALSE);

                return eventPlayer;
            })
            .orElseGet(() -> {
                    var eventPlayer = getEventPlayer(user, event);
                    event.joinPlayer(eventPlayer);

                    return eventPlayer;
                }
            );
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Player find(final Long chatId, final Long userId) {
        return playerRepository.findByUserIdAndChatId(userId, chatId)
            .orElseThrow(() -> new DataException(ExceptionCause.PLAYER_NOT_FOUND, userId, chatId));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Player getReference(Long id) {
        return referenceService.getReference(Player.class, id);
    }

    @Transactional
    public void signUp(final Long chatId, final Long userId) {
        var user = referenceService.getReference(UserInfo.class, userId);
        var group = referenceService.getReference(GroupInfo.class, chatId);
        var player = playerMapper.defaultPlayer(user, group);

        playerRepository.save(player);
    }

    @Override
    public AuthStatus authenticate(final SearchPlayerParams findParams) {
        return playerRepository.findByUserIdAndChatId(findParams.userId(), findParams.chatId())
            .map(player -> AuthStatus.SUCCESS)
            .orElse(AuthStatus.UNREGISTERED);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<TeamPlayer> mapTeamPlayers(List<TEventPlayer> players) {
        return playerMapper.mapTeamPlayers(players, this::getReference);
    }
}
