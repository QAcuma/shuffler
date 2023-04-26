package ru.acuma.shuffler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.BaseCommandHandler;

@Component
@RequiredArgsConstructor
public class NoCommand extends BaseBotCommand {

    private final BaseCommandHandler<NoCommand> commandHandler;

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }

    @Override
    public String getCommandIdentifier() {
        return Command.NO.getCommand();
    }
}

