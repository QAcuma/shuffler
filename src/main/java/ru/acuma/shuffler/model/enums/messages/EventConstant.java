package ru.acuma.shuffler.model.enums.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.service.buttons.EventConstantButton;

import static ru.acuma.shuffler.util.SymbolUtil.BLUE_CIRCLE;
import static ru.acuma.shuffler.util.SymbolUtil.CARAMEL;
import static ru.acuma.shuffler.util.SymbolUtil.GOBLET;
import static ru.acuma.shuffler.util.SymbolUtil.KEBABS;
import static ru.acuma.shuffler.util.SymbolUtil.MEMBERS;
import static ru.acuma.shuffler.util.SymbolUtil.NICE_MOON;
import static ru.acuma.shuffler.util.SymbolUtil.PONG;
import static ru.acuma.shuffler.util.SymbolUtil.PRIDE;
import static ru.acuma.shuffler.util.SymbolUtil.RED_CIRCLE;
import static ru.acuma.shuffler.util.SymbolUtil.SPACE_CLOCK;
import static ru.acuma.shuffler.util.SymbolUtil.WARNING;

@Getter
@AllArgsConstructor
public enum EventConstant implements EventConstantButton {

    BLANK_MESSAGE(""),
    SPACE_MESSAGE("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n"),
    LOBBY_MESSAGE_KICKER(KEBABS + " Время шашлыков! " + KEBABS + "\n" + SPACE_MESSAGE.getText() + MEMBERS + "Участники:\n"),
    LOBBY_MESSAGE_PING_PONG(PONG + " Время бить мяч! " + PONG + "\n" + SPACE_MESSAGE.getText() +
            MEMBERS + " Участники:\n"),
    LOBBY_PLAYING_MESSAGE(PRIDE + " Чемпионат в разгаре! " + PRIDE + "\n" + SPACE_MESSAGE.getText() +
            MEMBERS + " Участники:\n"),
    LOBBY_FINISHED_MESSAGE(PRIDE + " Чемпионат завершен! " + PRIDE + "\n" + SPACE_MESSAGE.getText() +
            MEMBERS + " Участники:\n"),
    LOBBY_WAITING_MESSAGE(SPACE_CLOCK + " Ждём игроков! " + SPACE_CLOCK + "\n" + SPACE_MESSAGE.getText() +
            MEMBERS + " Участники:\n"),
    WINNERS_MESSAGE("\n" + GOBLET + " Победители: \n"),
    GAME_MESSAGE("<b>Игра №</b>"),
    CANCEL_CHECKING_MESSAGE(WARNING + "️ Отменить чемпионат?\n"),
    NEXT_CHECKING_MESSAGE(WARNING + "️ Отменить текущую игру и начать новую?\n"),
    FINISH_CHECKING_MESSAGE(WARNING + "️ Завершить чемпионат?\n"),
    WAITING_MESSAGE(WARNING + "️ Недостаточно игроков для начала игры!\n"),
    BEGIN_CHECKING_MESSAGE(MEMBERS + " Все участники в сборе?\n"),
    RED_CHECKING_MESSAGE(RED_CIRCLE + "   Победа красных?   " + RED_CIRCLE),
    BLUE_CHECKING_MESSAGE(BLUE_CIRCLE + "   Победа синих?   " + BLUE_CIRCLE),
    LOBBY_CANCELED_MESSAGE("Чемпионат был отменен " + NICE_MOON);

    private final String text;
}
