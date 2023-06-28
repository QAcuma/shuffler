package ru.acuma.shuffler.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {

    MENU("menu"),
    EVENT("event"),
    JOIN("join"),
    LEAVE("leave"),
    KICK("kick"),
    CALL("call"),
    EVICT("evict"),
    CANCEL("cancel"),
    IDLE("idle"),
    YES("yes"),
    NO("no"),
    BEGIN("begin"),
    CANCEL_GAME("cancel_game"),
    CANCEL_EVICT("cancel_evict"),
    RESULT("result"),
    FINISH("finish"),
    RED("red"),
    BLUE("blue");

    private final String name;
}
