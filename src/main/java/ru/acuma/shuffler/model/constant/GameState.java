package ru.acuma.shuffler.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum GameState {

    NOT_EXIST(0),
    ACTIVE(1),
    CANCEL_CHECKING(1),
    RED_CHECKING(1),
    BLUE_CHECKING(1),
    CANCELLED(1),
    FINISHED(1);

    private final int rows;

    public boolean in(GameState... states) {
        return Arrays.asList(states).contains(this);
    }

}
