package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;

import static ru.acuma.k.shuffler.model.enums.messages.MessageType.GAME;

@Component
public class NoCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final MaintenanceService maintenanceService;
    private final EventStateService eventStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public NoCommand(EventContextServiceImpl eventContextService, MaintenanceService maintenanceService, EventStateService eventStateService, MessageService messageService, ExecuteService executeService) {
        super(Command.NO.getCommand(), "Нет");
        this.eventContextService = eventContextService;
        this.maintenanceService = maintenanceService;
        this.eventStateService = eventStateService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getEvent(message.getChatId());
        maintenanceService.sweepMessage(absSender, message);
        switch (event.getEventState()) {
            case CANCEL_LOBBY_CHECKING:
            case BEGIN_CHECKING:
                eventStateService.lobbyState(event);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case CANCEL_GAME_CHECKING:
            case RED_CHECKING:
            case BLUE_CHECKING:
            case FINISH_CHECKING:
                eventStateService.playingState(event);
                executeService.execute(absSender, messageService.updateMessage(event, event.getCurrentGame().getMessageId(), GAME));
                break;
        }
    }
}

