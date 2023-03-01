package ru.acuma.shuffler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.BaseCommandHandler;

@Component
public class CancelEvictCommand extends BaseBotCommand {

    private BaseCommandHandler<CancelEvictCommand> commandHandler;

    public CancelEvictCommand() {
        super(Command.CANCEL_EVICT.getCommand(), "Не исключать игрока");
    }

    @Autowired
    public void setCommandService(@Lazy BaseCommandHandler<CancelEvictCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }
}

