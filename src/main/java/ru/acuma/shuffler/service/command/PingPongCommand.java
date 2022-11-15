package ru.acuma.shuffler.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.service.CommandHandler;

@Component
public class PingPongCommand extends BaseBotCommand {

    private CommandHandler<PingPongCommand> commandHandler;

    @Autowired
    public void setCommandService(@Lazy CommandHandler<PingPongCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public PingPongCommand() {
        super(Command.PING_PONG.getCommand(), "Время пинать сферическую штуку");
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }
}
