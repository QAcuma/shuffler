package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.constant.GameState;
import ru.acuma.shuffler.model.constant.messages.MessageType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import static ru.acuma.shuffler.model.constant.GameState.FINISHED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TEvent implements Serializable {

    private Long id;
    private Long chatId;
    private EventStatus eventStatus;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Discipline discipline;
    private final List<TGame> tgGames = new ArrayList<>();
    private final Map<Long, TEventPlayer> players = new HashMap<>();
    @EqualsAndHashCode.Exclude
    private final Set<TRender> messages = new HashSet<>();
    @EqualsAndHashCode.Exclude
    private final transient List<Future<?>> futures = new ArrayList<>();
    @EqualsAndHashCode.Exclude
    private int hash;

    public TEvent snapshotHash() {
        hash = hashCode();

        return this;
    }

    public TEvent render(final TRender render) {
        Optional.ofNullable(render.getMessageId())
            .ifPresentOrElse(
                messageId -> replaceRender(render, messageId),
                () -> messages.add(render)
            );

        return this;
    }

    private void replaceRender(final TRender render, final Integer messageId) {
        messages.stream()
            .filter(message -> Objects.equals(messageId, message.getMessageId()))
            .findFirst()
            .ifPresent(messages::remove);
        messages.add(render);
    }

    public Integer getMessageId(final MessageType messageType) {
        return messages.stream()
            .filter(message -> Objects.equals(messageType, message.getMessageType()))
            .filter(message -> Objects.nonNull(message.getMessageId()))
            .findFirst()
            .map(TRender::getMessageId)
            .orElseThrow(() -> new DataException(ExceptionCause.MESSAGE_NOT_FOUND, messageType));
    }

    public boolean playerNotParticipate(Long userId) {
        var player = players.get(userId);
        return null == player || player.getEventContext().getLeft();
    }

    public void joinPlayer(TEventPlayer eventPlayer) {
        eventPlayer.getEventContext().setGameCount(getMaxGames());
        this.players.put(eventPlayer.getUserInfo().getUserId(), eventPlayer);
    }

    private Integer getMaxGames() {
        return getPlayers().values()
            .stream()
            .map(TEventPlayer::getEventContext)
            .mapToInt(TEventContext::getGameCount)
            .max()
            .orElse(0);
    }

    public void leaveLobby(final Long userId) {
        players.remove(userId);
    }

    public void leaveEvent(final Long userId) {
        players.get(userId)
            .getEventContext()
            .setLeft(true);
    }

    public List<TEventPlayer> getActivePlayers() {
        return players.values().stream()
            .filter(player -> !player.getEventContext().getLeft())
            .toList();
    }

    public boolean isPlayersEnough() {
        return getActivePlayers().size() >= Constants.GAME_PLAYERS_COUNT;
    }

    public TGame getCurrentGame() {
        return tgGames.stream()
            .max(Comparator.comparingInt(TGame::getIndex))
            .orElseThrow(() -> new DataException(ExceptionCause.GAME_NOT_FOUND, chatId));
    }

    public void applyGame(TGame tgGame) {
        tgGames.add(tgGame);
    }

    public Boolean isCalibrating() {
        return getTgGames().stream().anyMatch(TGame::isCalibrating);
    }

    public Boolean hasAnyGameFinished() {
        return getTgGames().stream().anyMatch(game -> game.getState() == FINISHED);
    }

    public GameState getLatestGameState() {
        var latestGame = getCurrentGame();
        return latestGame != null
               ? latestGame.getState()
               : GameState.NOT_EXIST;
    }

    public void scheduleFuture(ScheduledFuture<?> futureExecutor) {
        futures.add(futureExecutor);
    }

    public void cancelFutures() {
        futures.forEach(future -> future.cancel(true));
    }
}
