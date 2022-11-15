package ru.acuma.shuffler.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.service.CommandHandler;

@Component
public class BlueCommand extends BaseBotCommand {

    private CommandHandler<BlueCommand> commandHandler;

    @Autowired
    public void setCommandService(@Lazy CommandHandler<BlueCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public BlueCommand() {
        super(Command.BLUE.getCommand(), "Синие");
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }
}

