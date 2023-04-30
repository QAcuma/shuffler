package ru.acuma.shuffler.model.constant.keyboards.checking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.model.constant.Command;
import ru.acuma.shuffler.service.message.EventStatusButton;

@Getter
@AllArgsConstructor
public enum Checking2 implements EventStatusButton {

    BUTTON_YES(Command.IDLE.getCommand(), "2️⃣", 1),
    BUTTON_NO(Command.NO.getCommand(), "Нет", 1);

    public final String action;
    public final String alias;
    public final int row;

}
