package ru.acuma.shuffler.model.constant.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

    LOBBY,
    CHECKING,
    CHECKING_TIMED,
    CANCELLED,
    GAME,
    STAT;

}
