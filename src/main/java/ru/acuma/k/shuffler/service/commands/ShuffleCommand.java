package ru.acuma.k.shuffler.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.enums.Command;

@Component
public class ShuffleCommand extends BaseBotCommand {

    public ShuffleCommand() {
        super(Command.SHUFFLE.getCommand(), "Распределить команды");
    }

    @Override
    public void execute(AbsSender absSender, Message message) {


    }
}

