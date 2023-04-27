package ru.acuma.shuffler.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatType {

    GROUP("group"),
    PRIVATE("private");

    public final String value;
}
