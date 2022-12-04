package ru.acuma.shuffler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.CommandHandler;

@Component
public class KickCommand extends BaseBotCommand {

    private CommandHandler<KickCommand> commandHandler;

    @Autowired
    public void setCommandService(@Lazy CommandHandler<KickCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public KickCommand() {
        super(Command.KICK.getCommand(), "Исключить отсутствующего игрока");
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }

}

