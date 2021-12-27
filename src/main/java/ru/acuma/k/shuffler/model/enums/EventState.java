package ru.acuma.k.shuffler.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventState {

    CREATED,
    READY,
    CHECKING,
    PLAYING,
    FINISHED

}
