package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.util.BuildMessageUtil;

import java.util.Arrays;

@Component
public class NoCommand extends BaseBotCommand {

    private final EventContextService eventContextService;
    private final KeyboardService keyboardService;
    private final MaintenanceService maintenanceService;

    public NoCommand(EventContextService eventContextService, KeyboardService keyboardService, MaintenanceService maintenanceService) {
        super(Command.NO.getCommand(), "Нет");
        this.eventContextService = eventContextService;
        this.keyboardService = keyboardService;
        this.maintenanceService = maintenanceService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        var event = eventContextService.getEvent(chat.getId());
        maintenanceService.sweepFromArgs(absSender, arguments, chat.getId());
        switch (event.getEventState()) {
            case CANCEL_CHECKING:
                var newState = event.getMembers().size() >= eventContextService.getReadySize() ? EventState.READY : EventState.CREATED;
                event.setEventState(newState);
                break;
            case FINISH_CHECKING:
            case MEMBER_CHECKING:
                event.setEventState(EventState.PLAYING);
                break;
        }
        var keyboard = keyboardService.getKeyboard(event.getEventState());

        EditMessageText update = EditMessageText.builder()
                .chatId(String.valueOf(chat.getId()))
                .messageId(event.getBaseMessage())
                .text(BuildMessageUtil.buildCreatedMessage(eventContextService.getEvent(chat.getId())))
                .replyMarkup(keyboard)
                .build();

        absSender.execute(update);






    }
}

