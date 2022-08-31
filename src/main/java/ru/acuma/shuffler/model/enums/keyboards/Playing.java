package ru.acuma.shuffler.model.enums.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.buttons.EventStatusButton;

import static ru.acuma.shuffler.util.Symbols.AUTUMN_EMOJI;

@Getter
@AllArgsConstructor
public enum Playing implements EventStatusButton {

    BUTTON_JOIN(Command.JOIN.getCommand(), "Присоединиться к ребятам", 1),
    BUTTON_LEAVE(Command.LEAVE.getCommand(), "Уйти работать", 1),
    BUTTON_FINISH(Command.FINISH.getCommand(), "Завершить чемпионат", 2);

    public final String action;
    public final String alias;
    public final int row;

}
