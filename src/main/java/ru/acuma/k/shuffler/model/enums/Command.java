package ru.acuma.k.shuffler.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {

    KICKER("kicker"),
    JOIN("join"),
    LEAVE("leave"),
    CANCEL("cancel"),
    WAIT("wait"),
    YES("yes"),
    NO("no"),
    SHUFFLE("shuffle"),
    RESULT("result"),
    FINISH("finish"),
    RESET("reset"),
    BEGIN("begin"),
    RED("red"),
    BLUE("blue");

    private final String command;
}
