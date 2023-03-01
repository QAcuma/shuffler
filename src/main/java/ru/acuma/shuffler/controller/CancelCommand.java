package ru.acuma.shuffler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.BaseCommandHandler;

@Component
public class CancelCommand extends BaseBotCommand {

    private BaseCommandHandler<CancelCommand> commandHandler;

    @Autowired
    public void setCommandService(@Lazy BaseCommandHandler<CancelCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public CancelCommand() {
        super(Command.CANCEL.getCommand(), "Отменить турнир");
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }
}

