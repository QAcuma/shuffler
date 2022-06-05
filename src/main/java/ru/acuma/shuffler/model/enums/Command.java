package ru.acuma.shuffler.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {

    KICKER("kicker"),
    PING_PONG("ping_pong"),
    JOIN("join"),
    LEAVE("leave"),
    CANCEL("cancel"),
    WAIT("wait"),
    YES("yes"),
    NO("no"),
    BEGIN("begin"),
    CANCEL_GAME("cancel_game"),
    RESULT("result"),
    FINISH("finish"),
    RED("red"),
    BLUE("blue"),
    RESET("reset");

    private final String command;
}
