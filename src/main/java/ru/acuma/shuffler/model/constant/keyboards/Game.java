package ru.acuma.shuffler.model.constant.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.model.constant.Command;
import ru.acuma.shuffler.service.message.EventStatusButton;

import static ru.acuma.shuffler.util.Symbols.BLUE_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.RED_CIRCLE_EMOJI;

@Getter
@AllArgsConstructor
public enum Game implements EventStatusButton {

    BUTTON_JOIN(Command.RED.getCommand(), RED_CIRCLE_EMOJI, 1),
    BUTTON_LEAVE(Command.BLUE.getCommand(), BLUE_CIRCLE_EMOJI, 1),
    BUTTON_FINISH(Command.CANCEL_GAME.getCommand(), "Отменить игру", 2);

    public final String action;
    public final String alias;
    public final int row;

}
