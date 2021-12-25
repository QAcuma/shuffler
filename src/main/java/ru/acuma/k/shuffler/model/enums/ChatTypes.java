package ru.acuma.k.shuffler.model.enums;

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
