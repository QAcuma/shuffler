package ru.acuma.trgb.model.enums;

import lombok.Getter;

@Getter
public enum ChatTypes {

    GROUP("group"),
    PRIVATE("private");

    public String value;

    ChatTypes(String value) {
        this.value = value;
    }
}
