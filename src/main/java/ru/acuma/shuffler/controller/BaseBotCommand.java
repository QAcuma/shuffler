package ru.acuma.shuffler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.service.message.RenderService;

public abstract class BaseBotCommand {

    @Autowired
    private EventContext eventContext;

    @Autowired
    private RenderService renderService;

    public abstract String getCommandIdentifier();
    public abstract void execute(Message message, String... args);
}
