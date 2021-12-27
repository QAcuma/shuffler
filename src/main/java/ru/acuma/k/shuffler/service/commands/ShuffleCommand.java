package ru.acuma.k.shuffler.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.enums.Command;

@Component
public class ShuffleCommand extends BotCommand {

    public ShuffleCommand() {
        super(Command.SHUFFLE.getValue(), "Распределить команды");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        System.out.println(Command.SHUFFLE);
    }
}

