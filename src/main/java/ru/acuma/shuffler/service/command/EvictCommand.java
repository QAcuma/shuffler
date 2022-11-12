package ru.acuma.shuffler.service.command;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.api.PlayerService;
import ru.acuma.shuffler.service.api.UserService;
import ru.acuma.shuffler.service.facade.GameFacade;

@Component
public class EvictCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final MessageService messageService;
    private final UserService userService;
    private final ExecuteService executeService;
    private final MaintenanceService maintenanceService;
    private final PlayerService playerService;
    private final EventStateService eventStateService;
    private final GameService gameService;
    private final GameFacade gameFacade;

    public EvictCommand(EventContextServiceImpl eventContextService, MessageService messageService, UserService userService, ExecuteService executeService, MaintenanceService maintenanceService, PlayerService playerService, EventStateService eventStateService, GameService gameService, GameFacade gameFacade) {
        super(Command.EVICT.getCommand(), "Исключить отсутствующего игрока");
        this.eventContextService = eventContextService;
        this.maintenanceService = maintenanceService;
        this.userService = userService;
        this.messageService = messageService;
        this.executeService = executeService;
        this.playerService = playerService;
        this.eventStateService = eventStateService;
        this.gameService = gameService;
        this.gameFacade = gameFacade;
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
                playerService.leaveLobby(event, Long.valueOf(message.getText()));
                eventStateService.defineActiveState(event);
                executeService.execute(messageService.updateMessage(event, event.getBaseMessage(), MessageType.LOBBY));
                gameService.applyGameChecking(event);
                gameFacade.finishGameActions(event, message);
                eventStateService.defineActiveState(event);
                gameFacade.nextGameActions(event, message);
                break;
            default:
                break;
        }
    }

}

