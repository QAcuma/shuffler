package ru.acuma.shuffler.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameStatus {

    ACTIVE(1),
    CANCEL_CHECKING(1),
    EVICT_CHECKING(1),
    RED_CHECKING(1),
    BLUE_CHECKING(1),
    EVENT_CHECKING(0),
    CANCELLED(0),
    FINISHED(0);

    private final int rows;

}
