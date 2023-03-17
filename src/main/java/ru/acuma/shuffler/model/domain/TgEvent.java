package ru.acuma.shuffler.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shufflerlib.model.Discipline;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static ru.acuma.shuffler.model.enums.GameState.FINISHED;

@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class TgEvent implements Serializable {

    private final Set<Integer> messages = new TreeSet<>();
    private final Map<Long, TgEventPlayer> players = new HashMap<>();
    private final List<TgGame> tgGames = new ArrayList<>();
    private final List<Future<?>> futures = new ArrayList<>();
    private Long Id;
    private Long chatId;
    private EventState eventState;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Discipline discipline;

    public Integer getBaseMessage() {
        return Collections.min(this.messages);
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
                .collect(Collectors.toList());
    }

    public void spyMessage(Integer messageId) {
        this.messages.add(messageId);
    }

    public void missMessage(Integer messageId) {
        this.messages.remove(messageId);
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

    public void watchFuture(ScheduledFuture<?> futureExecutor) {
        futures.add(futureExecutor);
    }

    public void cancelFutures() {
        futures.forEach(future -> future.cancel(true));
    }

}
