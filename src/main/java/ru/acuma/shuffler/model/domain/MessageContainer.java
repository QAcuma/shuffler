package ru.acuma.shuffler.model.domain;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.io.Serializable;

@Getter
@Setter
public class MessageContainer<T extends BotApiMethod<Serializable>> {

    private Integer messageId;

    private T operation;



}
