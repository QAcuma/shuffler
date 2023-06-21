package ru.acuma.shuffler.model.constant.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.service.message.EventConstantButton;

import static ru.acuma.shuffler.util.Symbols.BLUE_CIRCLE_EMOJI;
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

    //    util
    BLANK_MESSAGE(""),
    SPACE_MESSAGE("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀"),
    SINGLE_SPACE(" "),
    //    menu
    MENU_MAIN_MESSAGE("Выберите дисциплину"),
    //    lobby
    LOBBY_MESSAGE_KICKER(SUMMER_EMOJI + " Подготовка к кикеру " + SUMMER_EMOJI + "\n"),
    LOBBY_MESSAGE_PING_PONG(PONG_EMOJI + " Подготовка к пинг-понгу " + PONG_EMOJI + "\n"),
    LOBBY_PLAYING_MESSAGE(SUMMER_EMOJI + " Игра в процессе " + SUMMER_EMOJI + "\n"),
    LOBBY_FINISHED_MESSAGE(SUMMER_EMOJI + " Сессия завершена " + SUMMER_EMOJI + "\n"),
    LOBBY_WAITING_MESSAGE(SAND_CLOCK_EMOJI + " Недостаточно игроков " + SAND_CLOCK_EMOJI + "\n"),
    LOBBY_NO_MEMBERS_TEXT("\n" + "Ожидание игроков!\n"),
    LOBBY_MEMBERS_TEXT("\n" + MEMBERS_EMOJI + "Игроки:\n"),
    LOBBY_WINNERS_MESSAGE("\n" + GOBLET_EMOJI + " Победители: \n"),
    LOBBY_SHUFFLER_LINK("\n" + "<i>Статистика игр: %s</i>"),
    LOBBY_CANCEL_CHECKING_MESSAGE("\n" + WARNING_EMOJI + "️ Отменить сессию?\n"),
    LOBBY_FINISH_CHECKING_MESSAGE("\n" + WARNING_EMOJI + "️ Завершить сессию?\n"),
    LOBBY_CANCELED_MESSAGE("Игры не состоялись " + NICE_MOON_EMOJI),
    //    game
    GAME_TITLE_MESSAGE("<b>Игра №</b>"),
    GAME_BEGIN_CHECKING_MESSAGE("\n" + WARNING_EMOJI + " Начать игру?\n"),
    GAME_CANCEL_CHECKING_MESSAGE("\n" + WARNING_EMOJI + " Отменить игру?"),
    GAME_WIN_CHECKING_MESSAGE(" Победили?"),
    GAME_RED_CHECKING_MESSAGE(RED_CIRCLE_EMOJI + GAME_WIN_CHECKING_MESSAGE),
    GAME_BLUE_CHECKING_MESSAGE(BLUE_CIRCLE_EMOJI + GAME_WIN_CHECKING_MESSAGE),
    //    evict
    EVICT_MESSAGE_TEXT(WARNING_EMOJI + " Выберите игрока для исключения");

    private final String text;

    @Override
    public String toString() {
        return getText();
    }
}
