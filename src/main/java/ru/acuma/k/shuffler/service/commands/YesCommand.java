package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.model.enums.messages.EventConstant;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.service.MaintenanceService;

import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class YesCommand extends BaseBotCommand {

    private final EventContextService eventContextService;

    private final MaintenanceService maintenanceService;

    public YesCommand(EventContextService eventContextService, KeyboardService keyboardService, MaintenanceService maintenanceService) {
        super(Command.YES.getCommand(), "Да");
        this.eventContextService = eventContextService;
        this.maintenanceService = maintenanceService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        var event = eventContextService.getEvent(chat.getId());
        Integer messageId;
        switch (event.getEventState()) {
            case CANCEL_CHECKING:
                maintenanceService.sweepChat(absSender, event.getMessages(), chat.getId());
                maintenanceService.sweepEvent(event, false);
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                SendMessage cancelled = SendMessage.builder()
                        .text(EventConstant.CANCEL_LOBBY_MESSAGE.getText())
                        .chatId(String.valueOf(chat.getId()))
                        .build();
                messageId = absSender.execute(cancelled).getMessageId();
                executorService.schedule(() -> maintenanceService.sweepChat(absSender, Collections.singleton(messageId), chat.getId()), 5, TimeUnit.MINUTES);
                break;
            case BEGIN_CHECKING:
                event.setEventState(EventState.PLAYING);
                SendMessage playing = SendMessage.builder()
                        .text(EventConstant.CANCEL_LOBBY_MESSAGE.getText())
                        .chatId(String.valueOf(chat.getId()))
                        .build();
                messageId = absSender.execute(playing).getMessageId();

                break;
            default:
        }


    }
}

