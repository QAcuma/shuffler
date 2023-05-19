package ru.acuma.shuffler.model.constant.keyboards;

import java.util.List;

public class Keyboards {

    public static final List<KeyboardButton> CHECKING_BUTTONS = List.of(
        ShufflerButton.BUTTON_YES,
        ShufflerButton.BUTTON_NO
    );

    public static final List<KeyboardButton> IDLE_BUTTONS = List.of(
        ShufflerButton.BUTTON_IDLE,
        ShufflerButton.BUTTON_NO
    );

    public static final List<KeyboardButton> IDLE_3_BUTTONS = List.of(
        ShufflerButton.BUTTON_IDLE_3,
        ShufflerButton.BUTTON_NO
    );

    public static final List<KeyboardButton> IDLE_2_BUTTONS = List.of(
        ShufflerButton.BUTTON_IDLE_2,
        ShufflerButton.BUTTON_NO
    );

    public static final List<KeyboardButton> IDLE_1_BUTTONS = List.of(
        ShufflerButton.BUTTON_IDLE_1,
        ShufflerButton.BUTTON_NO
    );

    public static final List<KeyboardButton> LOBBY_BUTTONS = List.of(
        ShufflerButton.BUTTON_JOIN,
        ShufflerButton.BUTTON_LEAVE,
        ShufflerButton.BUTTON_CANCEL
    );

    public static final List<KeyboardButton> LOBBY_PLAYING_BUTTONS = List.of(
        ShufflerButton.BUTTON_JOIN,
        ShufflerButton.BUTTON_LEAVE,
        ShufflerButton.BUTTON_FINISH
    );

    public static final List<KeyboardButton> LOBBY_READY_BUTTONS = List.of(
        ShufflerButton.BUTTON_JOIN,
        ShufflerButton.BUTTON_LEAVE,
        ShufflerButton.BUTTON_CANCEL,
        ShufflerButton.BUTTON_BEGIN
    );

    public static final List<KeyboardButton> GAME_BUTTONS = List.of(
        ShufflerButton.BUTTON_RED,
        ShufflerButton.BUTTON_BLUE,
        ShufflerButton.BUTTON_CANCEL_GAME
    );

    public static final List<KeyboardButton> MENU_DISCIPLINE_BUTTONS = List.of(
        ShufflerButton.BUTTON_KICKER,
        ShufflerButton.BUTTON_PING_PONG
    );
}
