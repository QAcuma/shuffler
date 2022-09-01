package ru.acuma.shuffler.model.enums.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.service.buttons.EventConstantButton;

import static ru.acuma.shuffler.util.Symbols.AUTUMN_EMOJI;
import static ru.acuma.shuffler.util.Symbols.BLUE_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.DRUM_EMOJI;
import static ru.acuma.shuffler.util.Symbols.GOBLET_EMOJI;
import static ru.acuma.shuffler.util.Symbols.KEBABS_EMOJI;
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
    LOBBY_MESSAGE_KICKER(KEBABS_EMOJI + " Время шашлыков! " + KEBABS_EMOJI + "\n"),
    LOBBY_MESSAGE_PING_PONG(PONG_EMOJI + " Время бить мяч! " + PONG_EMOJI + "\n"),
    LOBBY_PLAYING_MESSAGE(AUTUMN_EMOJI + " Чемпионат в разгаре! " + AUTUMN_EMOJI + "\n"),
    LOBBY_FINISHED_MESSAGE(AUTUMN_EMOJI + " Чемпионат завершен! " + AUTUMN_EMOJI + "\n"),
    LOBBY_WAITING_MESSAGE(SAND_CLOCK_EMOJI + " Ждём игроков! " + SAND_CLOCK_EMOJI + "\n"),
    WINNERS_MESSAGE("\n" + GOBLET_EMOJI + " Победители: \n"),
    GAME_MESSAGE("<b>Игра №</b>"),
    MEMBERS_TEXT("\n" + MEMBERS_EMOJI + "Участники:\n"),
    LET_JOIN_TEXT("\n" + DRUM_EMOJI + "Присоединяйся к чемпионату!\n"),
    CANCEL_CHECKING_MESSAGE(WARNING_EMOJI + "️ Отменить чемпионат?\n"),
    NEXT_CHECKING_MESSAGE(WARNING_EMOJI + "️ Отменить текущую игру и начать новую?\n"),
    FINISH_CHECKING_MESSAGE(WARNING_EMOJI + "️ Завершить чемпионат?\n"),
    WAITING_MESSAGE(WARNING_EMOJI + "️ Недостаточно игроков для начала игры!\n"),
    BEGIN_CHECKING_MESSAGE(MEMBERS_EMOJI + " Все участники в сборе?\n"),
    RED_CHECKING_MESSAGE(RED_CIRCLE_EMOJI + "   Победа красных?   " + RED_CIRCLE_EMOJI),
    BLUE_CHECKING_MESSAGE(BLUE_CIRCLE_EMOJI + "   Победа синих?   " + BLUE_CIRCLE_EMOJI),
    SHUFFLER_LINK("\n" + "Статистика игр: shuffler.fun"),
    LOBBY_CANCELED_MESSAGE("Чемпионат был отменен " + NICE_MOON_EMOJI);

    private final String text;
}
