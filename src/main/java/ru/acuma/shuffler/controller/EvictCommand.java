package ru.acuma.shuffler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.BaseCommandHandler;

@Component
public class EvictCommand extends BaseBotCommand {

    private BaseCommandHandler<EvictCommand> commandHandler;

    public EvictCommand() {
        super(Command.EVICT.getCommand(), "Исключить отсутствующего игрока");
    }

    @Autowired
    public void setCommandService(@Lazy BaseCommandHandler<EvictCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }

}
