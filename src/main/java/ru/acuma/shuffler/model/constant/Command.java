package ru.acuma.shuffler.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {

    EVENT("event"),
    JOIN("join"),
    LEAVE("leave"),
    KICK("kick"),
    EVICT("evict"),
    CANCEL("cancel"),
    WAIT("wait"),
    YES("yes"),
    NO("no"),
    BEGIN("begin"),
    CANCEL_GAME("cancel_game"),
    CANCEL_EVICT("cancel_evict"),
    RESULT("result"),
    FINISH("finish"),
    RED("red"),
    BLUE("blue"),
    RESET("reset");

    private final String command;
}
