package ru.acuma.shuffler.model.enums.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.buttons.EventStatusButton;

import static ru.acuma.shuffler.util.SymbolUtil.BLUE_CIRCLE;
import static ru.acuma.shuffler.util.SymbolUtil.RED_CIRCLE;

@Getter
@AllArgsConstructor
public enum Game implements EventStatusButton {

    BUTTON_JOIN(Command.RED.getCommand(), RED_CIRCLE, 1),
    BUTTON_LEAVE(Command.BLUE.getCommand(), BLUE_CIRCLE, 1),
    BUTTON_FINISH(Command.CANCEL_GAME.getCommand(), "Отменить игру", 2);

    public final String action;
    public final String alias;
    public final int row;

}
