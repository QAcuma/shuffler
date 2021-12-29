package ru.acuma.k.shuffler.model.entity;

import lombok.Builder;
import lombok.Data;
import ru.acuma.k.shuffler.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    public boolean isPresent(Long playerId) {
        return players.containsKey(playerId);
    }

    public void joinPlayer(KickerPlayer kickerPlayer) {
        this.players.put(kickerPlayer.getId(), new KickerEventPlayer(kickerPlayer));
    }

    public void leavePlayer(Long playerId) {
        this.players.remove(playerId);
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

}
