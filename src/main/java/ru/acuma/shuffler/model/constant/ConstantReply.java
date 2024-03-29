package ru.acuma.shuffler.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstantReply {

    SERVICE_UNAVAILABLE("Прости малышка, сервис недоступен для тебя"),
    UNSUPPORTED_COMMAND("Команда не поддерживается"),
    PONG("pong");

    public final String value;

}
