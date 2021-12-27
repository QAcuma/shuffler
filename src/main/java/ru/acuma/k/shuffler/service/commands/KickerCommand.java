package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.util.BuildMessageUtil;

import java.util.Arrays;
import java.util.Collections;

@Component
public class KickerCommand extends BaseBotCommand {

    private final EventContextService eventContextService;

    private final KeyboardService keyboardService;

    private final MaintenanceService maintenanceService;

    public KickerCommand(EventContextService eventContextService, KeyboardService keyboardService, MaintenanceService maintenanceService) {
        super(Command.KICKER.getCommand(), "Время покрутить шашлыки");
        this.eventContextService = eventContextService;
        this.keyboardService = keyboardService;
        this.maintenanceService = maintenanceService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        maintenanceService.sweepFromArgs(absSender, arguments, chat.getId());
        if (eventContextService.isActive(chat.getId())) {
            return;
        }
        var event = eventContextService.buildEvent(chat.getId());
        var keyboard = keyboardService.getKeyboard(event.getEventState());

        SendMessage response = SendMessage.builder()
                .text(BuildMessageUtil.buildCreatedMessage(event))
                .chatId(String.valueOf(event.getGroupId()))
                .replyMarkup(keyboard)
                .build();

        event.getMessages().add(absSender.execute(response).getMessageId());
    }
}
