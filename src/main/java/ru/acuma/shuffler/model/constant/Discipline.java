package ru.acuma.shuffler.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.acuma.shuffler.exception.DataException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Discipline {

    KICKER("kicker", "Кикер"),
    PING_PONG("pong", "Пинг-понг");

    private final String name;
    private final String label;

    public static Discipline getByName(final String name) {
        return Arrays.stream(Discipline.values())
            .filter(discipline -> discipline.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new DataException(ExceptionCause.DISCIPLINE_NOT_PRESENT, name, name));
    }
}
