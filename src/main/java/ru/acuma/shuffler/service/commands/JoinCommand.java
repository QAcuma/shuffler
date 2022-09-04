package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.api.PlayerService;
import ru.acuma.shuffler.service.facade.GameFacade;

@Component
public class JoinCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final MaintenanceService maintenanceService;
    private final EventStateService eventStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final GameFacade gameFacade;

    public JoinCommand(EventContextServiceImpl eventContextService, MaintenanceService maintenanceService, EventStateService eventStateService, MessageService messageService, ExecuteService executeService, PlayerService playerService, GameService gameService, GameFacade gameFacade) {
        super(Command.JOIN.getCommand(), "Присоединиться к игре");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.gameFacade = gameFacade;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());

        if (event.isCallbackUnauthorized(message.getFrom().getId())) {
            return;
        }
        switch (event.getEventState()) {
            case CREATED:
            case READY:
                playerService.authenticate(event, message.getFrom());
                eventStateService.definePreparingState(event);
                executeService.execute(absSender, messageService.updateMessage(event, event.getBaseMessage(), MessageType.LOBBY));
                break;
            case WAITING:
                playerService.joinLobby(event, message.getFrom());
                eventStateService.defineActiveState(event);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                gameFacade.nextGameActions(absSender, event, message);
                break;
            case WAITING_WITH_GAME:
                playerService.joinLobby(event, message.getFrom());
                eventStateService.defineActiveState(event);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case PLAYING:
                playerService.joinLobby(event, message.getFrom());
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
        }
    }
}

