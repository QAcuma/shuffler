package ru.acuma.shuffler.service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.service.CommandHandler;

@Slf4j
@Component
public class ResetCommand extends BaseBotCommand {

    private CommandHandler<ResetCommand> commandHandler;

    @Autowired
    public void setCommandService(@Lazy CommandHandler<ResetCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public ResetCommand() {
        super(Command.RESET.getCommand(), "Сбросить текущий эвент");
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }
}

