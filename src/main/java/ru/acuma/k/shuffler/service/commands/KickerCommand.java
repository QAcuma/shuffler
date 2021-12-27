package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventHolder;
import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.model.enums.keyboards.Created;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.util.BuildMessageUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class KickerCommand extends BotCommand {

    @Autowired
    private EventHolder eventHolder;

    @Autowired
    private KeyboardService keyboardService;

    public KickerCommand() {
        super(Command.KICKER.getValue(), "Время покрутить шашлыки");
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (eventHolder.isActive(chat.getId())) {
            return;
        }

        KickerEvent event = KickerEvent.builder()
                .eventState(EventState.CREATED)
                .groupId(chat.getId())
                .startedAt(LocalDateTime.now())
                .build();

        eventHolder.addEvent(event);

        var keyboard = keyboardService.getKeyboard(chat.getId());
        SendMessage response = SendMessage.builder()
                .text(BuildMessageUtil.buildCreatedMessage(eventHolder.getEvent(chat.getId())))
                .chatId(chat.getId().toString())
                .replyMarkup(keyboard)
                .build();
        absSender.execute(response);
    }
}
