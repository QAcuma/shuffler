package ru.acuma.k.shuffler.model.enums.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.enums.EventStatusApi;

@Getter
@AllArgsConstructor
public enum Playing implements EventStatusApi {

    BUTTON_JOIN(Command.JOIN.getValue(), "Присоединиться к ребятам"),
    BUTTON_LEAVE(Command.LEAVE.getValue(), "Уйти работать"),
    BUTTON_FINISH(Command.FINISH.getValue(), "Завершить чемпионат");

    public final String action;
    public final String alias;

}
