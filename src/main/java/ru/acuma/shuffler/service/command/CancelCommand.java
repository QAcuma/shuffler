package ru.acuma.shuffler.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.service.CommandService;

@Component
public class CancelCommand extends BaseBotCommand {

    private CommandService<CancelCommand> commandService;

    @Autowired
    public void setCommandService(@Lazy CommandService<CancelCommand> commandService) {
        this.commandService = commandService;
    }

    public CancelCommand() {
        super(Command.CANCEL.getCommand(), "Отменить турнир");
    }

    @Override
    public void execute(Message message) {
        commandService.handle(message);
    }
}

