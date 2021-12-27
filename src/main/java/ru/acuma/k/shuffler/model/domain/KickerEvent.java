package ru.acuma.k.shuffler.model.domain;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class KickerEvent {

    private Long Id;

    private Long groupId;

    private Integer messageId;

    private EventState eventState;

    private String messageText;

    private final List<User> members = new ArrayList<>();

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

}
