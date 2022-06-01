package ru.acuma.shuffler.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventState {

    CREATED(2),
    CANCEL_LOBBY_CHECKING(1),
    CANCELLED(0),
    READY(3),
    BEGIN_CHECKING(1),
    PLAYING(2),
    WAITING(0),
    CANCEL_GAME_CHECKING(1),
    RED_CHECKING(1),
    BLUE_CHECKING(1),
    MEMBER_CHECKING(1),
    FINISH_CHECKING(1),
    FINISHED(2);

    private final int rows;

}
