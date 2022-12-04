package ru.acuma.shuffler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.CommandHandler;

@Component
public class YesCommand extends BaseBotCommand {

    private CommandHandler<YesCommand> commandHandler;

    @Autowired
    public void setCommandService(@Lazy CommandHandler<YesCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public YesCommand() {
        super(Command.YES.getCommand(), "Да");
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }

}

