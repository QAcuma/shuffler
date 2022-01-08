package ru.acuma.k.shuffler.model.enums.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.service.enums.EventConstantApi;

@Getter
@AllArgsConstructor
public enum EventConstant implements EventConstantApi {

    BLANK_MESSAGE(""),
    LOBBY_MESSAGE("🍢 Самое время крутить шашлыки\\! 🍢\n" +
            "\n" +
            "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66Участники:\n"),
    LOBBY_PLAYING_MESSAGE("🍢 Чемпионат в самом разгаре\\! 🍢\n" +
            "\n" +
            "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66Участники:\n"),
    LOBBY_FINISHED_MESSAGE("🍢 Чемпионат завершен\\! 🍢\n" +
            "\n" +
            "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66Участники:\n"),
    CANCEL_CHECKING_MESSAGE("Точно завершить чемпионат? \n"),
    BEGIN_CHECKING_MESSAGE("Перекличка\\! Все участники в сборе? \uD83D\uDC65\n"),
    NEXT_CHECKING_MESSAGE("⚽️Отменить текущую игру и начать новую? ⚽️\n"),
    RED_CHECKING_MESSAGE("🔴 Засчитать победу красных и начать новую игру? 🔴\n"),
    BLUE_CHECKING_MESSAGE("🔵 Засчитать победу синих и начать новую игру? 🔵\n"),
    LOBBY_CANCELED_MESSAGE("Чемпионат был отменен \uD83C\uDF1A"),
    STAT_MESSAGE(""),
    MEMBER_CHECKING_MESSAGE(", ты точно хочешь покинуть игру?"),
    UNEXPECTED_CHECKING_MESSAGE("Завершить игру?"),
    FINISH_CHECKING_MESSAGE("\uD83D\uDC25 Завершить чемпионат? \uD83D\uDC25"),
    GAME_MESSAGE("Игра №"),
    STARTED_GAME_MESSAGE("Отметьте победителя после окончания матча\\!"),
    CANCELLED_GAME_MESSAGE("Игра была отменена"),
    WINNERS_MESSAGE("\n\n\uD83C\uDFC6Победители: \n");

    private final String text;

}
