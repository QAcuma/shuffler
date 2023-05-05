package ru.acuma.shuffler.model.constant.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.model.constant.Command;

import static ru.acuma.shuffler.util.Symbols.BLUE_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.CLOCK_EMOJI;
import static ru.acuma.shuffler.util.Symbols.RED_CIRCLE_EMOJI;

@Getter
@AllArgsConstructor
public enum ShufflerButton {

    BUTTON_JOIN(Command.JOIN.getCommand(), "Присоединиться", 1),
    BUTTON_LEAVE(Command.LEAVE.getCommand(), "Покинуть", 2),
    BUTTON_CANCEL(Command.CANCEL.getCommand(), "Отменить", 2),
    BUTTON_BEGIN(Command.BEGIN.getCommand(), "Начать", 3),
    BUTTON_RED(Command.RED.getCommand(), RED_CIRCLE_EMOJI, 1),
    BUTTON_BLUE(Command.BLUE.getCommand(), BLUE_CIRCLE_EMOJI, 1),
    BUTTON_CANCEL_GAME(Command.CANCEL_GAME.getCommand(), "Отменить", 2),
    BUTTON_IDLE_3(Command.IDLE.getCommand(), "3️⃣", 1),
    BUTTON_IDLE_2(Command.IDLE.getCommand(), "2️⃣", 1),
    BUTTON_IDLE_1(Command.IDLE.getCommand(), "1️⃣", 1),
    BUTTON_IDLE(Command.IDLE.getCommand(), CLOCK_EMOJI, 1),
    BUTTON_YES(Command.YES.getCommand(), "Да", 1),
    BUTTON_NO(Command.NO.getCommand(), "Нет", 1),
    BUTTON_FINISH(Command.FINISH.getCommand(), "Завершить", 2);

    public final String action;
    public final String alias;
    public final int row;
}
