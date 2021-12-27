package ru.acuma.k.shuffler.model.enums.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.service.enums.EventConstantApi;

@Getter
@AllArgsConstructor
public enum EventConstant implements EventConstantApi {

    LOBBY_MESSAGE("🍢 Самое время крутить шашлыки! 🍢\n" +
            "\n" +
            "Участники:\n"),
    CANCEL_CHECKING_MESSAGE("Точно завершить чемпионат? \n"),
    CANCEL_LOBBY_MESSAGE("Игра была отменена \uD83C\uDF1A"),
    MEMBER_CHECKING_MESSAGE(", ты точно хочешь покинуть игру?"),
    UNEXPECTED_CHECKING_MESSAGE("Завершить игру?"),
    FINISH_CHECKING_MESSAGE("За работу?");

    private final String text;

}
