package ru.acuma.shuffler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.CommandHandler;

@Component
public class KickerCommand extends BaseBotCommand {

    private CommandHandler<KickerCommand> commandHandler;

    @Autowired
    public void setCommandService(@Lazy CommandHandler<KickerCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public KickerCommand() {
        super(Command.KICKER.getCommand(), "Время покрутить шашлыки");
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }
}
