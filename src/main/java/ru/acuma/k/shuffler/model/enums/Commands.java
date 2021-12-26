package ru.acuma.k.shuffler.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Commands {

    KICKER("/kicker"),
    JOIN("/join"),
    LEAVE("/leave"),
    SHUFFLE("/shuffle"),
    BEGIN("/begin");

    private final String value;
}
