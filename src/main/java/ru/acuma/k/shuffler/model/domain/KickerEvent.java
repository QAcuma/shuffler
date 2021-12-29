package ru.acuma.k.shuffler.model.domain;

import lombok.Builder;
import lombok.Data;
import ru.acuma.k.shuffler.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class KickerEvent {

    private Long Id;

    private Long chatId;

    private final Set<Integer> messages = new TreeSet<>();

    private EventState eventState;

    private final Set<EventPlayer> players = new HashSet<>();

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private final List<KickerGame> games = new ArrayList<>();

    public Integer getBaseMessage() {
        return Collections.min(this.messages);
    }

}
