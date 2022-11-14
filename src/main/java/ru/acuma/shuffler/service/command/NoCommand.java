package ru.acuma.shuffler.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.service.CommandService;

@Component
public class NoCommand extends BaseBotCommand {

    private CommandService<NoCommand> commandService;

    public NoCommand() {
        super(Command.NO.getCommand(), "Нет");
    }

    @Autowired
    public void setCommandService(@Lazy CommandService<NoCommand> commandService) {
        this.commandService = commandService;
    }

    @Override
    public void execute(Message message) {
        commandService.handle(message);
    }
}

