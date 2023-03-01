package ru.acuma.shuffler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.BaseCommandHandler;

@Component
public class LeaveCommand extends BaseBotCommand {

    private BaseCommandHandler<LeaveCommand> commandHandler;

    @Autowired
    public void setCommandService(@Lazy BaseCommandHandler<LeaveCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public LeaveCommand() {
        super(Command.LEAVE.getCommand(), "Покинуть список участников");
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }

}

