package ru.acuma.k.shuffler.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.acuma.k.shuffler.model.enums.GameState;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Accessors(chain = true)
public class KickerGame {

    private Long id;

    private int index;

    private int messageId;

    private GameState state;

    private KickerTeam redTeam;

    private KickerTeam blueTeam;

    private KickerTeam winnerTeam;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

}
