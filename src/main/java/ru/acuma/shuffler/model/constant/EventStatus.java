package ru.acuma.shuffler.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EventStatus {

    ANY(0, true),
    CREATED(2, true),
    CANCEL_CHECKING(1, true),
    READY(3, true),
    BEGIN_CHECKING(1, true),
    PLAYING(2, true),
    WAITING_WITH_GAME(0, true),
    WAITING(0, true),
    GAME_CHECKING(0, true),
    FINISH_CHECKING(1, true),
    EVICTING(0, true),
    CANCELLED(0, false),
    FINISHED(2, false);

    private final int markupRows;
    private final boolean isActive;

    public boolean in(EventStatus... states) {
        return Arrays.asList(states).contains(this);
    }

}
