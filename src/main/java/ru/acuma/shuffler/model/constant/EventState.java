package ru.acuma.shuffler.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EventState {

    CREATED(2),
    READY(3),
    CANCEL_CHECKING(1),
    CANCELLED(0),
    BEGIN_CHECKING(1),
    PLAYING(2),
    WAITING(0),
    GAME_CHECKING(0),
    WAITING_WITH_GAME(0),
    FINISH_CHECKING(1),
    FINISHED(2),
    EVICTING(0),
    ANY(0);

    private final int rows;

    public boolean in(EventState... states) {
        return Arrays.asList(states).contains(this);
    }

}
