package ru.acuma.shuffler.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.model.constant.GameState;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.service.message.Render;
import ru.acuma.shufflerlib.model.Discipline;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import static ru.acuma.shuffler.model.constant.GameState.FINISHED;

@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class TgEvent implements Serializable {

    private Long id;
    private Long chatId;
    private EventState eventState;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Discipline discipline;
    private final Map<Long, TgEventPlayer> players = new HashMap<>();
    private final List<TgGame> tgGames = new ArrayList<>();
    private final EnumMap<MessageType, Render> messages = new EnumMap<>(MessageType.class);
    private final transient List<Future<?>> futures = new ArrayList<>();

    public void action(final MessageType messageType, final Render render) {
        messages.put(messageType, render);
    }

    public Integer getLobbyMessageId() {
        return messages.get(MessageType.LOBBY).getMessageId();
    }

    public boolean playerNotParticipate(Long telegramId) {
        var player = players.get(telegramId);
        return null == player || player.getEventContext().getLeft();
    }

    public void joinPlayer(TgEventPlayer eventPlayer) {
        this.players.put(eventPlayer.getUserInfo().getTelegramId(), eventPlayer);
    }

    public void leaveLobby(Long telegramId) {
        this.players.remove(telegramId);
    }

    public List<TgEventPlayer> getActivePlayers() {
        return players.values().stream()
            .filter(player -> !player.getEventContext().getLeft())
            .toList();
    }

    public TgGame getLatestGame() {
        return tgGames.stream()
            .max(Comparator.comparingInt(TgGame::getIndex))
            .orElse(null);
    }

    public void applyGame(TgGame tgGame) {
        tgGames.add(tgGame);
    }

    public Boolean isCalibrating() {
        return getTgGames().stream().anyMatch(TgGame::isCalibrating);
    }

    public Boolean hasAnyGameFinished() {
        return getTgGames().stream().anyMatch(game -> game.getState() == FINISHED);
    }

    public GameState getLatestGameState() {
        var latestGame = getLatestGame();
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
