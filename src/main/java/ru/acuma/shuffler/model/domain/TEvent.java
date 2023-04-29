package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.constant.GameState;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shufflerlib.model.Discipline;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final EnumMap<MessageType, Render> deletes = new EnumMap<>(MessageType.class);
    @EqualsAndHashCode.Exclude
    private final EnumMap<MessageType, Render> messages = new EnumMap<>(MessageType.class);
    @EqualsAndHashCode.Exclude
    private final transient List<Future<?>> futures = new ArrayList<>();
    @EqualsAndHashCode.Exclude
    private int hash;

    public TEvent snapshotHash() {
        hash = hashCode();

        return this;
    }

    public TEvent render(final MessageType messageType, final Render render) {
        messages.put(messageType, render);

        return this;
    }

    public TEvent delete(final MessageType messageType) {
        Optional.ofNullable(messages.remove(messageType))
            .ifPresent(message -> deletes.put(
                messageType,
                Render.forDelete(message.getMessageId())
            ));

        return this;
    }

    public TEvent delete(final Integer messageId) {
        deletes.put(MessageType.OTHER, Render.forDelete(messageId));

        return this;
    }

    public Integer getLobbyMessageId() {
        return messages.get(MessageType.LOBBY).getMessageId();
    }

    public boolean playerNotParticipate(Long telegramId) {
        var player = players.get(telegramId);
        return null == player || player.getEventContext().getLeft();
    }

    public TEvent joinPlayer(TEventPlayer eventPlayer) {
        eventPlayer.getEventContext().setGameCount(getMaxGames());
        this.players.put(eventPlayer.getUserInfo().getTelegramId(), eventPlayer);

        return this;
    }

    private Integer getMaxGames() {
        return getPlayers().values()
            .stream()
            .map(TEventPlayer::getEventContext)
            .mapToInt(TEventContext::getGameCount)
            .max()
            .orElse(0);
    }

    public void leaveLobby(Long telegramId) {
        this.players.remove(telegramId);
    }

    public List<TEventPlayer> getActivePlayers() {
        return players.values().stream()
            .filter(player -> !player.getEventContext().getLeft())
            .toList();
    }

    public boolean enoughPlayers() {
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
