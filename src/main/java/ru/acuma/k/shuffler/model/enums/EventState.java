package ru.acuma.k.shuffler.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventState {

    CREATED(2),
    CANCEL_CHECKING(1),
    READY(3),
    BEGIN_CHECKING(1),
    PLAYING(2),
    MEMBER_CHECKING(1),
    FINISH_CHECKING(1),
    FINISHED(2);

    private final int rows;

}
