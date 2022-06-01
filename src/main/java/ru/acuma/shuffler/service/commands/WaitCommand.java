package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.model.enums.Command;

@Component
public class WaitCommand extends BaseBotCommand {

    public WaitCommand() {
        super(Command.WAIT.getCommand(), "Ожидание доступности кнопки");
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        return;
    }
}

