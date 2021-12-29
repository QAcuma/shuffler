package ru.acuma.k.shuffler.model.entity;

import lombok.Data;
import ru.acuma.k.shuffler.model.enums.GameState;

import java.time.LocalDateTime;

@Data
public class KickerGame {

    private Long id;

    private GameState state;

    private KickerTeam red;

    private KickerTeam blue;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;


}
