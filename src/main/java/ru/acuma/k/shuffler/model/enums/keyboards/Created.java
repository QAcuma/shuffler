package ru.acuma.k.shuffler.model.enums.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.enums.EventStatusApi;

@Getter
@AllArgsConstructor
public enum Created implements EventStatusApi {

    BUTTON_JOIN(Command.JOIN.getValue(), "Играю!"),
    BUTTON_LEAVE(Command.LEAVE.getValue(), "Не смогу :(");

    public final String action;
    public final String alias;

}
