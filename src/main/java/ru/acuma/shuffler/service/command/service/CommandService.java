package ru.acuma.shuffler.service.command.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.service.command.BaseBotCommand;

import javax.annotation.PostConstruct;

public abstract class CommandService<T extends BaseBotCommand> {

    @PostConstruct
    protected abstract void init();

    public abstract Class<T> getCommandClass();

    public abstract void doExecute(Message message);

}
