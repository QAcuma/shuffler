package ru.acuma.shuffler.model.enums.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.service.buttons.EventConstantButton;

import static ru.acuma.shuffler.util.Symbols.AUTUMN_EMOJI;
import static ru.acuma.shuffler.util.Symbols.BLUE_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.GOBLET_EMOJI;
import static ru.acuma.shuffler.util.Symbols.MEMBERS_EMOJI;
import static ru.acuma.shuffler.util.Symbols.NICE_MOON_EMOJI;
import static ru.acuma.shuffler.util.Symbols.PONG_EMOJI;
import static ru.acuma.shuffler.util.Symbols.RED_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.SAND_CLOCK_EMOJI;
import static ru.acuma.shuffler.util.Symbols.WARNING_EMOJI;

@Getter
@AllArgsConstructor
public enum EventConstant implements EventConstantButton {

    BLANK_MESSAGE(""),
    SPACE_MESSAGE("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀"),
    LOBBY_MESSAGE_KICKER(AUTUMN_EMOJI + " Готовимся к игре! " + AUTUMN_EMOJI + "\n"),
    LOBBY_MESSAGE_PING_PONG(PONG_EMOJI + " Время бить мяч! " + PONG_EMOJI + "\n"),
    LOBBY_PLAYING_MESSAGE(AUTUMN_EMOJI + " Чемпионат в разгаре! " + AUTUMN_EMOJI + "\n"),
    LOBBY_FINISHED_MESSAGE(AUTUMN_EMOJI + " Чемпионат завершен! " + AUTUMN_EMOJI + "\n"),
    LOBBY_WAITING_MESSAGE(SAND_CLOCK_EMOJI + " Ждём игроков! " + SAND_CLOCK_EMOJI + "\n"),
    WINNERS_MESSAGE("\n" + GOBLET_EMOJI + " Победители: \n"),
    GAME_MESSAGE("<b>Игра №</b>"),
    MEMBERS_TEXT("\n" + MEMBERS_EMOJI + "Участники:\n"),
    LET_JOIN_TEXT("\n" + "Присоединяйся к чемпионату!\n"),
    CANCEL_CHECKING_MESSAGE(WARNING_EMOJI + "️ Отменить чемпионат?\n"),
    NEXT_CHECKING_MESSAGE(WARNING_EMOJI + "️ Отменить текущую игру и начать новую?\n"),
    FINISH_CHECKING_MESSAGE(WARNING_EMOJI + "️ Завершить чемпионат?\n"),
    WAITING_MESSAGE("\n" + WARNING_EMOJI + "<i>Недостаточно игроков для начала игры!</i>\n"),
    BEGIN_CHECKING_MESSAGE(" Все участники в сборе?\n"),
    RED_CHECKING_MESSAGE(RED_CIRCLE_EMOJI + "   Победа красных?   " + RED_CIRCLE_EMOJI),
    BLUE_CHECKING_MESSAGE(BLUE_CIRCLE_EMOJI + "   Победа синих?   " + BLUE_CIRCLE_EMOJI),
    CALIBRATING_MESSAGE("\n" + "<i>* - Ставка некоторых игр была понижена из-за участия неоткалиброванных игроков </i>\n"),
    SHUFFLER_LINK("\n" + "<i>Статистика игр: shuffler.fun</i>"),
    LOBBY_CANCELED_MESSAGE("Чемпионат был отменен " + NICE_MOON_EMOJI);

    private final String text;
}
