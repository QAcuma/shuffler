package ru.acuma.k.shuffler.model.enums.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.service.enums.EventConstantApi;

@Getter
@AllArgsConstructor
public enum EventConstant implements EventConstantApi {

    BLANK_MESSAGE(""),
    SPACE_MESSAGE("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n"),
    LOBBY_MESSAGE("🍢 Время шашлычного межсезонья! 🍢\n" + SPACE_MESSAGE.getText() +
            "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66 Участники:\n"),
    LOBBY_PLAYING_MESSAGE("🍢 Межсезонье! Игры не повлияют на рейтинг! 🍢\n" + SPACE_MESSAGE.getText() +
            "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66 Участники:\n"),
    LOBBY_FINISHED_MESSAGE("🍢 Серия игр завершена! 🍢\n" + SPACE_MESSAGE.getText() +
            "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66 Участники:\n"),
    LOBBY_WAITING_MESSAGE("⏳ Ждём игроков! ⏳\n" + SPACE_MESSAGE.getText() +
            "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66 Участники:\n"),
    WINNERS_MESSAGE("\n\uD83C\uDFC6Победители: \n"),
    GAME_MESSAGE("<b>Игра №</b>"),
    CANCEL_CHECKING_MESSAGE("⚠️ Отменить чемпионат?\n"),
    NEXT_CHECKING_MESSAGE("⚠️ Отменить текущую игру и начать новую?\n"),
    FINISH_CHECKING_MESSAGE("⚠️ Завершить чемпионат?\n"),
    WAITING_MESSAGE("⚠️ Недостаточно игроков для начала игры!\n"),
    BEGIN_CHECKING_MESSAGE("\uD83D\uDC65 Все участники в сборе?\n"),
    RED_CHECKING_MESSAGE("\uD83D\uDD3A   Победа красных?   \uD83D\uDD3A\n"),
    BLUE_CHECKING_MESSAGE("\uD83D\uDD39   Победа синих?   \uD83D\uDD39\n"),
    LOBBY_CANCELED_MESSAGE("Чемпионат был отменен \uD83C\uDF1A");

    private final String text;
}
