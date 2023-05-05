package ru.acuma.shuffler.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameState {

    ACTIVE(1),
    CANCEL_CHECKING(1),
    RED_CHECKING(1),
    BLUE_CHECKING(1),
    CANCELLED(1),
    FINISHED(1);

    private final int rows;

}
