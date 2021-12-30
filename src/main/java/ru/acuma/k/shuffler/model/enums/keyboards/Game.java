package ru.acuma.k.shuffler.model.enums.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.enums.EventStatusApi;

@Getter
@AllArgsConstructor
public enum Game implements EventStatusApi {

    BUTTON_JOIN(Command.JOIN.getCommand(), "♦️", 1),
    BUTTON_LEAVE(Command.LEAVE.getCommand(), "\uD83D\uDD37", 1),
    BUTTON_FINISH(Command.FINISH.getCommand(), "Отменить игру", 2);

    public final String action;
    public final String alias;
    public final int row;

}