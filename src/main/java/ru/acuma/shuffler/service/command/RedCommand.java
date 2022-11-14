package ru.acuma.shuffler.service.command;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.service.CommandService;

@Component
public class RedCommand extends BaseBotCommand {

    private CommandService<RedCommand> commandService;

    @Autowired
    public void setCommandService(@Lazy CommandService<RedCommand> commandService) {
        this.commandService = commandService;
    }

    public RedCommand() {
        super(Command.RED.getCommand(), "Красные");
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        commandService.handle(message);
    }
}

