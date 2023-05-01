package ru.acuma.shuffler.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.acuma.shuffler.exception.DataException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Discipline {

    KICKER("kicker"),
    PING_PONG("pong");

    private final String webName;

    public static Discipline getByWebName(final String webName) {
        return Arrays.stream(Discipline.values())
            .filter(discipline -> discipline.getWebName().equals(webName))
            .findFirst()
            .orElseThrow(() -> new DataException(ExceptionCause.DISCIPLINE_NOT_PRESENT, webName, webName));
    }
}
