package ru.acuma.shuffler.service.command.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.service.command.BaseBotCommand;
import ru.acuma.shuffler.service.executor.Executor;

import javax.annotation.PostConstruct;

public abstract class CommandService<T extends BaseBotCommand> {

    @Autowired
    protected Executor executor;

    @PostConstruct
    protected abstract void init();

    public abstract Class<T> getCommandClass();

    public void handle(Message message) {
        executor.doExecute(message, getCommandClass());
    }

}
