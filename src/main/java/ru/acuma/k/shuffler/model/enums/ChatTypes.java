package ru.acuma.k.shuffler.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatTypes {

    GROUP("group"),
    PRIVATE("private");

    public final String value;
}
