package ru.acuma.shuffler.model.enums.keyboards.checking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.buttons.EventStatusButton;

@Getter
@AllArgsConstructor
public enum Checking implements EventStatusButton {

    BUTTON_YES(Command.YES.getCommand(), "Да", 1),
    BUTTON_NO(Command.NO.getCommand(), "Нет", 1);

    public final String action;
    public final String alias;
    public final int row;

}
