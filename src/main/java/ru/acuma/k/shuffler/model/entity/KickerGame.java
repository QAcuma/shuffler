package ru.acuma.k.shuffler.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.acuma.k.shuffler.model.enums.GameState;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class KickerGame {

    private Long id;

    private int index;

    private GameState state;

    private KickerTeam redTeam;

    private KickerTeam blueTeam;

    private KickerTeam winnerTeam;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;


}
