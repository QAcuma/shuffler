package ru.acuma.k.shuffler.model.enums.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.enums.EventStatusApi;

@Getter
@AllArgsConstructor
public enum Checking implements EventStatusApi {

    BUTTON_YES(Command.YES.getValue(), "Да"),
    BUTTON_NO(Command.NO.getValue(), "Нет");

    public final String action;
    public final String alias;

}
