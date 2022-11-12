package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

@Component
public class CancelEvictCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final GameStateService gameStateService;
    private final EventStateService eventStateService;
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final MaintenanceService maintenanceService;

    public CancelEvictCommand(EventContextServiceImpl eventContextService, GameStateService gameStateService, EventStateService eventStateService, ExecuteService executeService, MessageService messageService, MaintenanceService maintenanceService) {
        super(Command.CANCEL_EVICT.getCommand(), "Не исключать игрока");
        this.eventContextService = eventContextService;
        this.gameStateService = gameStateService;
        this.eventStateService = eventStateService;
        this.executeService = executeService;
        this.messageService = messageService;
        this.maintenanceService = maintenanceService;
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        maintenanceService.sweepMessage(message);
        final var event = eventContextService.getCurrentEvent(message.getChatId());
        if (event == null || event.playerNotParticipate(message.getFrom().getId())) {
            return;
        }
        switch (event.getEventState()) {
            case EVICTING:
                eventStateService.defineActiveState(event);
                gameStateService.activeState(event.getLatestGame());

                executeService.execute(messageService.updateLobbyMessage(event));
                executeService.execute(messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME));
        }
    }
}

