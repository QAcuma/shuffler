package ru.acuma.shuffler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.BaseCommandHandler;

@Component
public class BlueCommand extends BaseBotCommand {

    private BaseCommandHandler<BlueCommand> commandHandler;

    public BlueCommand() {
        super(Command.BLUE.getCommand(), "Синие");
    }

    @Autowired
    public void setCommandService(@Lazy BaseCommandHandler<BlueCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }
}

