package ru.acuma.shuffler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.command.BaseCommandHandler;

@Component
@RequiredArgsConstructor
public class EventCommand extends BaseBotCommand {

    private final BaseCommandHandler<EventCommand> commandHandler;

    @Override
    public void execute(Message message) {
        commandHandler.handle(message);
    }

    @Override
    public String getCommandIdentifier() {
        return Command.EVENT.getCommand();
    }
}
