package ru.acuma.shuffler.service.command;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.BaseBotCommand;
import ru.acuma.shuffler.model.enums.EventState;

import java.util.List;

public abstract class BaseCommandHandler<T extends BaseBotCommand> {

    protected abstract List<EventState> getSupportedStates();

    public abstract void handle(Message message);

}
