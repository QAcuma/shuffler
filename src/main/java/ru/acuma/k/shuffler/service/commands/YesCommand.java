package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.model.enums.messages.EventConstant;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ru.acuma.k.shuffler.model.enums.Values.CANCELLED_MESSAGE_TIMEOUT;

@Component
public class YesCommand extends BaseBotCommand {

    private final EventContextService eventContextService;
    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public YesCommand(EventContextService eventContextService, EventStateService eventStateService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService) {
        super(Command.YES.getCommand(), "Да");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        var event = eventContextService.getEvent(chat.getId());
        switch (event.getEventState()) {
            case CANCEL_CHECKING:
                cancelChampionship(absSender, event);
                break;
            case BEGIN_CHECKING:
                break;
            default:
        }
    }

    @SneakyThrows
    private void cancelChampionship(AbsSender absSender, KickerEvent event) {
        eventStateService.cancelledState(event);
        executeService.execute(absSender, messageService.updateLobbyMessage(event));

        var messages = Collections.singleton(event.getBaseMessage());
        long chatId = event.getChatId();

        eventContextService.unregisterMessage(event.getChatId(), event.getBaseMessage());
        maintenanceService.sweepChat(absSender, event.getMessages(), event.getChatId());
        maintenanceService.sweepEvent(event, false);

        Runnable method = () -> maintenanceService.sweepChat(absSender, messages, chatId);
        executeService.executeAsync(absSender, method, CANCELLED_MESSAGE_TIMEOUT);
    }
}

