package ru.acuma.shuffler.model.entity;

import lombok.Builder;
import lombok.Data;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shufflerlib.model.Discipline;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Data
@Builder
public class TgEvent {

    private Long Id;

    private Long chatId;

    private final Set<Integer> messages = new TreeSet<>();

    private EventState eventState;

    private final Map<Long, TgEventPlayer> players = new HashMap<>();

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private final List<TgGame> tgGames = new ArrayList<>();

    private Discipline discipline;

    public Integer getBaseMessage() {
        return Collections.min(this.messages);
    }

    public boolean isPresent(Long telegramId) {
        var player = players.get(telegramId);
        return player != null && !player.isLeft();
    }

    public void joinPlayer(TgEventPlayer tgEventPlayer) {
        this.players.put(tgEventPlayer.getTelegramId(), tgEventPlayer);
    }

    public void leaveLobby(Long telegramId) {
        this.players.remove(telegramId);
    }

    public List<TgEventPlayer> getActivePlayers() {
        return players.values().stream()
                .filter(player -> !player.isLeft())
                .collect(Collectors.toList());
    }

    public void watchMessage(Integer messageId) {
        this.messages.add(messageId);
    }

    public void missMessage(Integer messageId) {
        this.messages.remove(messageId);
    }

    public TgGame getCurrentGame() {
        return tgGames.stream()
                .max(Comparator.comparingInt(TgGame::getIndex))
                .orElse(null);
    }

    public void newGame(TgGame tgGame) {
        tgGames.add(tgGame);
    }

}
