package ru.acuma.shuffler.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.service.CommandService;

@Component
public class YesCommand extends BaseBotCommand {

    private CommandService<YesCommand> commandService;

    @Autowired
    public void setCommandService(@Lazy CommandService<YesCommand> commandService) {
        this.commandService = commandService;
    }

    public YesCommand() {
        super(Command.YES.getCommand(), "Да");
    }

    @Override
    public void execute(Message message) {
        commandService.handle(message);
    }

}

