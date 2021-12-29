package ru.acuma.k.shuffler.model.enums.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.service.enums.EventConstantApi;

@Getter
@AllArgsConstructor
public enum EventConstant implements EventConstantApi {

    DEFAULT_MESSAGE(""),
    LOBBY_MESSAGE("🍢 Самое время крутить шашлыки! 🍢\n" +
            "\n" +
            "Участники:\n"),
    LOBBY_PLAYING_MESSAGE("🍢 Турнир в самом разгаре! 🍢\n" +
            "\n" +
            "Участники:\n"),
    CANCEL_CHECKING_MESSAGE("Точно завершить чемпионат? \n"),
    BEGIN_CHECKING_MESSAGE("Перекличка! Все участники в сборе? \uD83D\uDC65\n"),
    LOBBY_CANCELED_MESSAGE("Игра была отменена \uD83C\uDF1A"),
    PLAYING_MESSAGE(""),
    STAT_MESSAGE(""),
    MEMBER_CHECKING_MESSAGE(", ты точно хочешь покинуть игру?"),
    UNEXPECTED_CHECKING_MESSAGE("Завершить игру?"),
    FINISH_CHECKING_MESSAGE("За работу?"),
    LOBBY_FINISHED_MESSAGE("🍢 Славно поиграли! 🍢\n" +
            "\n" +
            "Участники:\n");

    private final String text;

}
