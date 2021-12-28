package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;

import static ru.acuma.k.shuffler.model.enums.messages.MessageType.LOBBY;

@Component
public class NoCommand extends BaseBotCommand {

    private final EventContextService eventContextService;
    private final MaintenanceService maintenanceService;
    private final EventStateService eventStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public NoCommand(EventContextService eventContextService, MaintenanceService maintenanceService, EventStateService eventStateService, MessageService messageService, ExecuteService executeService) {
        super(Command.NO.getCommand(), "Нет");
        this.eventContextService = eventContextService;
        this.maintenanceService = maintenanceService;
        this.eventStateService = eventStateService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        var event = eventContextService.getEvent(chat.getId());
        maintenanceService.sweepFromArgs(absSender, arguments, chat.getId());
        switch (event.getEventState()) {
            case CANCEL_CHECKING:
            case BEGIN_CHECKING:
                eventStateService.lobbyState(event);
                break;
            case FINISH_CHECKING:
                eventStateService.playingState(event);
                break;
        }
        executeService.execute(absSender, messageService.updateLobbyMessage(event));
    }
}

