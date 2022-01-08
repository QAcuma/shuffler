package ru.acuma.k.shuffler.model.entity;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.SerializationUtils;
import ru.acuma.k.shuffler.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class KickerEvent {

    private Long Id;

    private Long chatId;

    private final Set<Integer> messages = new TreeSet<>();

    private EventState eventState;

    private final Map<Long, KickerEventPlayer> players = new HashMap<>();

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private final List<KickerGame> games = new ArrayList<>();

    public Integer getBaseMessage() {
        return Collections.min(this.messages);
    }

    public boolean isPresent(Long telegramId) {
        return players.containsKey(telegramId);
    }

    public void joinPlayer(KickerEventPlayer kickerPlayer) {
        this.players.put(kickerPlayer.getTelegramId() + 1, SerializationUtils.clone(kickerPlayer).setGameCount(0));
        this.players.put(kickerPlayer.getTelegramId() + 2, SerializationUtils.clone(kickerPlayer).setGameCount(0));
        this.players.put(kickerPlayer.getTelegramId() + 3, SerializationUtils.clone(kickerPlayer).setGameCount(0));
        this.players.put(kickerPlayer.getTelegramId() + 4, SerializationUtils.clone(kickerPlayer).setGameCount(0));
        this.players.put(kickerPlayer.getTelegramId(), kickerPlayer);
    }

    public void leaveLobby(Long telegramId) {
        this.players.remove(telegramId);
    }

    public boolean isPresent(Integer messageId) {
        return messages.contains(messageId);
    }

    public void watchMessage(Integer messageId) {
        this.messages.add(messageId);
    }

    public void missMessage(Integer messageId) {
        this.messages.remove(messageId);
    }

    public KickerGame getCurrentGame() {
        return games.stream()
                .max(Comparator.comparingInt(KickerGame::getIndex))
                .orElse(null);
    }

    public void newGame(KickerGame game) {
        games.add(game);
    }

}
