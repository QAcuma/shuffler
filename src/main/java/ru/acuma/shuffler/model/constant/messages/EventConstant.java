package ru.acuma.shuffler.model.constant.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.service.message.EventConstantButton;

import static ru.acuma.shuffler.util.Symbols.BLUE_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.DICE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.GOBLET_EMOJI;
import static ru.acuma.shuffler.util.Symbols.MEMBERS_EMOJI;
import static ru.acuma.shuffler.util.Symbols.NICE_MOON_EMOJI;
import static ru.acuma.shuffler.util.Symbols.PONG_EMOJI;
import static ru.acuma.shuffler.util.Symbols.RED_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.SAND_CLOCK_EMOJI;
import static ru.acuma.shuffler.util.Symbols.SUMMER_EMOJI;
import static ru.acuma.shuffler.util.Symbols.WARNING_EMOJI;

@Getter
@AllArgsConstructor
public enum EventConstant implements EventConstantButton {

    BLANK_MESSAGE(""),
    SPACE_MESSAGE("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀"),
    SINGLE_SPACE(" "),
    MENU_MAIN_MESSAGE("Выберите дисциплину"),
    LOBBY_MESSAGE_KICKER(SUMMER_EMOJI + " Готовимся к игре! " + SUMMER_EMOJI + "\n"),
    LOBBY_MESSAGE_PING_PONG(PONG_EMOJI + " Время бить мяч! " + PONG_EMOJI + "\n"),
    LOBBY_MESSAGE_DEFAULT(DICE_EMOJI + " Время распределяться по командам! " + DICE_EMOJI + "\n"),
    LOBBY_PLAYING_MESSAGE(SUMMER_EMOJI + " Чемпионат в разгаре! " + SUMMER_EMOJI + "\n"),
    LOBBY_FINISHED_MESSAGE(SUMMER_EMOJI + " Чемпионат завершен! " + SUMMER_EMOJI + "\n"),
    LOBBY_WAITING_MESSAGE(SAND_CLOCK_EMOJI + " Недостаточно игроков для начала игры! " + SAND_CLOCK_EMOJI + "\n"),
    WINNERS_MESSAGE("\n" + GOBLET_EMOJI + " Победители: \n"),
    GAME_MESSAGE("<b>Игра №</b>"),
    MEMBERS_TEXT("\n" + MEMBERS_EMOJI + "Участники:\n"),
    LET_JOIN_TEXT("\n" + "Присоединяйся к чемпионату!\n"),
    EVICT_MESSAGE_TEXT("Выберите игрока для исключения"),
    EVICT_PLAYER_CHECKING("\n" + "Исключить игрока?!\n"),
    CANCEL_CHECKING_MESSAGE("\n" + WARNING_EMOJI + "️ Отменить событие?\n"),
    NEXT_CHECKING_MESSAGE("Начать следующую игру?"),
    FINISH_CHECKING_MESSAGE("\n" + WARNING_EMOJI + "️ Завершить событие?\n"),
    BEGIN_CHECKING_MESSAGE("\n" + " Начать игру?\n"),
    WIN_CHECKING_MESSAGE("Победили?"),
    RED_CHECKING_MESSAGE(RED_CIRCLE_EMOJI + WIN_CHECKING_MESSAGE),
    BLUE_CHECKING_MESSAGE(BLUE_CIRCLE_EMOJI + WIN_CHECKING_MESSAGE),
    SHUFFLER_LINK("\n" + "<i>Статистика игр: shuffler.host</i>"),
    LOBBY_CANCELED_MESSAGE("Чемпионат был отменен " + NICE_MOON_EMOJI);

    private final String text;
}
