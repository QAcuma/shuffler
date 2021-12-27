package ru.acuma.k.shuffler.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatType {

    GROUP("group"),
    PRIVATE("private");

    public final String value;
}
