package ru.acuma.k.shuffler.model.enums.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.enums.EventStatusApi;

@Getter
@AllArgsConstructor
public enum Finished implements EventStatusApi {

    BUTTON_RESULT(Command.RESULT.getCommand(), "Играю!", 1);

    public final String action;
    public final String alias;
    public final int row;

}
