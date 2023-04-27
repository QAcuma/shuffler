package ru.acuma.shuffler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.constant.Command;
import ru.acuma.shuffler.service.command.BaseCommandHandler;

@Component
@RequiredArgsConstructor
public class KickCommand extends BaseBotCommand {

    private final BaseCommandHandler<KickCommand> commandHandler;

    @Override
    public void execute(final Message message, final String... args) {
        commandHandler.handle(message);
    }

    @Override
    public String getCommandIdentifier() {
        return Command.KICK.getCommand();
    }
}

