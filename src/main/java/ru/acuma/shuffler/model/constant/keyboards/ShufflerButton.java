package ru.acuma.shuffler.model.constant.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.model.constant.Command;

import static ru.acuma.shuffler.util.Symbols.BLUE_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.CLOCK_EMOJI;
import static ru.acuma.shuffler.util.Symbols.RED_CIRCLE_EMOJI;

@Getter
@AllArgsConstructor
public enum ShufflerButton implements KeyboardButton {

    BUTTON_KICKER(Command.EVENT.getName() + "?discipline=kicker", "Кикер", 1),
    BUTTON_PING_PONG(Command.EVENT.getName() + "?discipline=pong", "Пинг-понг", 2),
    BUTTON_JOIN(Command.JOIN.getName(), "Присоединиться", 1),
    BUTTON_LEAVE(Command.LEAVE.getName(), "Покинуть", 2),
    BUTTON_CANCEL(Command.CANCEL.getName(), "Отменить", 2),
    BUTTON_BEGIN(Command.BEGIN.getName(), "Начать", 3),
    BUTTON_RED(Command.RED.getName(), RED_CIRCLE_EMOJI, 1),
    BUTTON_BLUE(Command.BLUE.getName(), BLUE_CIRCLE_EMOJI, 1),
    BUTTON_CANCEL_GAME(Command.CANCEL_GAME.getName(), "Отменить", 2),
    BUTTON_IDLE_3(Command.IDLE.getName(), "3️⃣", 1),
    BUTTON_IDLE_2(Command.IDLE.getName(), "2️⃣", 1),
    BUTTON_IDLE_1(Command.IDLE.getName(), "1️⃣", 1),
    BUTTON_IDLE(Command.IDLE.getName(), CLOCK_EMOJI, 1),
    BUTTON_YES(Command.YES.getName(), "Да", 1),
    BUTTON_NO(Command.NO.getName(), "Нет", 1),
    BUTTON_FINISH(Command.FINISH.getName(), "Завершить", 2);

    public final String callback;
    public final String text;
    public final int row;
}
