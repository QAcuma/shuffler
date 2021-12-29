package ru.acuma.k.shuffler.model.domain;

import lombok.Data;
import ru.acuma.k.shuffler.model.enums.GameState;

import java.time.LocalDateTime;

@Data
public class KickerGame {

    private Long id;

    private GameState state;

    private Team red;

    private Team blue;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;



}
