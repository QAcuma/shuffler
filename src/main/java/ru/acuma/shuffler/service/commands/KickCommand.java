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
import ru.acuma.shuffler.service.api.KickService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

@Component
public class KickCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final ExecuteService executeService;
    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;
    private final KickService kickService;
    private final MessageService messageService;
    private final GameStateService gameStateService;

    public KickCommand(EventContextServiceImpl eventContextService, ExecuteService executeService, EventStateService eventStateService, MaintenanceService maintenanceService, KickService kickService, MessageService messageService, GameStateService gameStateService) {
        super(Command.KICK.getCommand(), "Исключить отсутствующего игрока");
        this.eventContextService = eventContextService;
        this.maintenanceService = maintenanceService;
        this.eventStateService = eventStateService;
        this.executeService = executeService;
        this.kickService = kickService;
        this.messageService = messageService;
        this.gameStateService = gameStateService;
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
            case PLAYING:
            case WAITING:
            case WAITING_WITH_GAME:
                eventStateService.evictingState(event);
                gameStateService.cancelCheckingState(event.getLatestGame());
                var method = kickService.prepareKickMessage(event);

                executeService.execute(method);
                executeService.execute(messageService.updateLobbyMessage(event));
                executeService.execute(messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME));
        }
    }

}

