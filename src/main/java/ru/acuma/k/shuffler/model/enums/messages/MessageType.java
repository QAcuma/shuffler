package ru.acuma.k.shuffler.model.enums.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

    LOBBY,
    CHECKING,
    CHECKING_TIMED,
    GAME,
    STAT;

}
