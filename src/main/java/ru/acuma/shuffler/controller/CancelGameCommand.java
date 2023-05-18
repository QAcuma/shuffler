package ru.acuma.shuffler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.constant.Command;
import ru.acuma.shuffler.service.command.BaseCommandHandler;

@Component
@RequiredArgsConstructor
public class CancelGameCommand implements BaseBotCommand {

    private final BaseCommandHandler<CancelGameCommand> commandHandler;

    @Override
    public void execute(final Message message, final String... args) {
        commandHandler.handle(message, args);
    }

    @Override
    public String getCommandIdentifier() {
        return Command.CANCEL_GAME.getName();
    }
}

