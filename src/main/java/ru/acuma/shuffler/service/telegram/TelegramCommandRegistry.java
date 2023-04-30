package ru.acuma.shuffler.service.telegram;

import org.springframework.stereotype.Component;
import ru.acuma.shuffler.controller.BaseBotCommand;
import ru.acuma.shuffler.model.constant.Command;

import java.util.HashMap;
import java.util.Map;

@Component
public class TelegramCommandRegistry {

    private final Map<String, BaseBotCommand> commandRegistry;

    public TelegramCommandRegistry() {
        this.commandRegistry = new HashMap<>();
    }

    public void register(final BaseBotCommand command) {
        commandRegistry.put(command.getCommandIdentifier(), command);
    }

    public BaseBotCommand resolve(final String commandIdentifier) {
        var pureCommand = commandIdentifier.replace("/", "");

        return commandRegistry.getOrDefault(
            pureCommand,
            commandRegistry.get(Command.IDLE.getCommand())
        );
    }
}
