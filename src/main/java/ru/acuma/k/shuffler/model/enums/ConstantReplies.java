package ru.acuma.k.shuffle.model.enums;

import lombok.Getter;

@Getter
public enum ConstantReplies {

    SERVICE_UNAVAILABLE("Прости малышка, сервис недоступен для тебя"),
    PONG("pong");

    public String value;

    ConstantReplies(String value) {
        this.value = value;
    }


}
