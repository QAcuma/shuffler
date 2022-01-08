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
import java.util.concurrent.ThreadLocalRandom;

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
        var player = players.get(telegramId);
        if (player != null && !player.isLeft()) {
            return true;
        }
        return false;
    }

    public void joinPlayer(KickerEventPlayer kickerPlayer) {
        var player1 = SerializationUtils.clone(kickerPlayer).setGameCount(0);
        var player2 = SerializationUtils.clone(kickerPlayer).setGameCount(0);
        var player3 = SerializationUtils.clone(kickerPlayer).setGameCount(0);
        var player4 = SerializationUtils.clone(kickerPlayer).setGameCount(0);
        player1.setRating(ThreadLocalRandom.current().nextInt(650, 1350))
                .setTelegramId(123L)
                .setFirstName("Player")
                .setLastName("First");
        player2.setRating(ThreadLocalRandom.current().nextInt(650, 1350))
                .setTelegramId(124L)
                .setFirstName("Player")
                .setLastName("Second");
        player3.setRating(ThreadLocalRandom.current().nextInt(650, 1350))
                .setTelegramId(125L)
                .setFirstName("Player")
                .setLastName("Third");
        player4.setRating(ThreadLocalRandom.current().nextInt(650, 1350))
                .setTelegramId(126L)
                .setFirstName("Player")
                .setLastName("Fourth");
        this.players.put(player1.getTelegramId(), player1);
        this.players.put(player2.getTelegramId(), player2);
        this.players.put(player3.getTelegramId(), player3);
        this.players.put(player4.getTelegramId(), player4);
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
