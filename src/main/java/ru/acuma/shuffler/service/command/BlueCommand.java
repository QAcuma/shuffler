package ru.acuma.shuffler.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.service.CommandService;

@Component
public class BlueCommand extends BaseBotCommand {

    private CommandService<BlueCommand> commandService;

    @Autowired
    public void setCommandService(@Lazy CommandService<BlueCommand> commandService) {
        this.commandService = commandService;
    }

    public BlueCommand() {
        super(Command.BLUE.getCommand(), "Синие");
    }

    @Override
    public void execute(Message message) {
        commandService.handle(message);
    }
}

