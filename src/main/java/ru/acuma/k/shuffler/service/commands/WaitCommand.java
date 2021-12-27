package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.enums.Command;

@Component
public class WaitCommand extends BaseBotCommand {

    public WaitCommand() {
        super(Command.WAIT.getCommand(), "Ожидание доступности кнопки");
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        return;
    }
}
