package ru.acuma.shuffler.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.BaseCommandHandler;

@Slf4j
@Component
public class ResetCommand extends BaseBotCommand {

    private BaseCommandHandler<ResetCommand> commandHandler;

    public ResetCommand() {
        super(Command.RESET.getCommand(), "Сбросить текущий эвент");
    }

    @Autowired
    public void setCommandService(@Lazy BaseCommandHandler<ResetCommand> commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }
}

