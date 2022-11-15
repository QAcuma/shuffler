package ru.acuma.shuffler.service.command;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.service.CommandHandler;

@Component
public class JoinCommand extends BaseBotCommand {

    private CommandHandler<JoinCommand> commandHandler;

    @Autowired
    public void setCommandService(@Lazy CommandHandler<JoinCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public JoinCommand() {
        super(Command.JOIN.getCommand(), "Присоединиться к игре");
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }
}

