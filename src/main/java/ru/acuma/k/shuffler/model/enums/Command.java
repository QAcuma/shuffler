package ru.acuma.k.shuffler.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {

    START("start"),
    KICKER("kicker"),
    JOIN("join"),
    LEAVE("leave"),
    SHUFFLE("shuffle"),
    YES("yes"),
    NO("no"),
    RESULT("result"),
    FINISH("finish"),
    BEGIN("begin");

    private final String value;
}
