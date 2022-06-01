package ru.acuma.shuffler.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameState {

    STARTED,
    CHECKING,
    CANCELLED,
    FINISHED;

}
