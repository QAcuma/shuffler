package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.enums.Commands;

@Component
public class BeginCommand extends BotCommand {

    public BeginCommand() {
        super(Commands.BEGIN.getValue(), "Начать игру");
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        System.out.println(Commands.BEGIN);
        absSender.execute(new SendMessage(chat.getId().toString(), Commands.BEGIN.toString()));
    }
}
