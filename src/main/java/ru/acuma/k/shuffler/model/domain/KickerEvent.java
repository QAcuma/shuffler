package ru.acuma.k.shuffler.model.domain;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class KickerEvent {

    private Long Id;

    private Long groupId;

    private final Set<Integer> messages = new TreeSet<>();

    private EventState eventState;

    private final List<User> members = new LinkedList<>();

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    public Integer getBaseMessage() {
        return Collections.min(this.messages);
    }

}
