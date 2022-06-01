package ru.acuma.shuffler.model.entity;

import lombok.Builder;
import lombok.Data;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shufflerlib.model.Discipline;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
public class GameEvent {

    private Long Id;

    private Long chatId;

    private final Set<Integer> messages = new TreeSet<>();

    private EventState eventState;

    private final Map<Long, GameEventPlayer> players = new HashMap<>();

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private final List<Game> games = new ArrayList<>();

    private Discipline discipline;

    public Integer getBaseMessage() {
        return Collections.min(this.messages);
    }

    public boolean isPresent(Long telegramId) {
        var player = players.get(telegramId);
        return player != null && !player.isLeft();
    }

    public void joinPlayer(GameEventPlayer kickerPlayer) {
//        var player1 = SerializationUtils.clone(kickerPlayer).setGameCount(0);
//        var player2 = SerializationUtils.clone(kickerPlayer).setGameCount(0);
//        var player3 = SerializationUtils.clone(kickerPlayer).setGameCount(0);
//        var player4 = SerializationUtils.clone(kickerPlayer).setGameCount(0);
//        var player5 = SerializationUtils.clone(kickerPlayer).setGameCount(0);
//        player1.setRating(ThreadLocalRandom.current().nextInt(650, 1350))
//                .setTelegramId(123L)
//                .setFirstName("Player")
//                .setLastName("A");
//        player2.setRating(ThreadLocalRandom.current().nextInt(650, 1350))
//                .setTelegramId(124L)
//                .setFirstName("Player")
//                .setLastName("B");
//        player3.setRating(ThreadLocalRandom.current().nextInt(650, 1350))
//                .setTelegramId(125L)
//                .setFirstName("Player")
//                .setLastName("C");
//        player4.setRating(ThreadLocalRandom.current().nextInt(650, 1350))
//                .setTelegramId(126L)
//                .setFirstName("Player")
//                .setLastName("D");
//        player5.setRating(ThreadLocalRandom.current().nextInt(650, 1350))
//                .setTelegramId(127L)
//                .setFirstName("Player")
//                .setLastName("F");
//        this.players.put(player1.getTelegramId(), player1);
//        this.players.put(player2.getTelegramId(), player2);
//        this.players.put(player3.getTelegramId(), player3);
//        this.players.put(player4.getTelegramId(), player4);
//        this.players.put(player5.getTelegramId(), player5);
        this.players.put(kickerPlayer.getTelegramId(), kickerPlayer);
    }

    public void leaveLobby(Long telegramId) {
        this.players.remove(telegramId);
    }

    public boolean isPresent(Integer messageId) {
        return messages.contains(messageId);
    }

    public List<GameEventPlayer> getActivePlayers() {
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

    public Game getCurrentGame() {
        return games.stream()
                .max(Comparator.comparingInt(Game::getIndex))
                .orElse(null);
    }

    public void newGame(Game game) {
        games.add(game);
    }

}
