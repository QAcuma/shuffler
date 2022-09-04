package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.api.ChampionshipService;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.facade.EventFacade;
import ru.acuma.shuffler.service.facade.GameFacade;

@Component
public class YesCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final ChampionshipService championshipService;
    private final GameService gameService;
    private final GameFacade gameFacade;
    private final EventFacade eventFacade;

    public YesCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService, ChampionshipService championshipService, GameService gameService, GameFacade gameFacade, EventFacade eventFacade) {
        super(Command.YES.getCommand(), "Да");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
        this.championshipService = championshipService;
        this.gameService = gameService;
        this.gameFacade = gameFacade;
        this.eventFacade = eventFacade;
    }

    @Override
    @SneakyThrows
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());
        maintenanceService.sweepMessage(absSender, message);

        if (event == null) {
            return;
        }

        switch (event.getEventState()) {
            case CANCEL_CHECKING:
                championshipService.finishEvent(absSender, event);
                break;
            case BEGIN_CHECKING:
                gameFacade.nextGameActions(absSender, event, message);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case PLAYING:
                gameService.applyGameChecking(event);
                gameFacade.finishGameActions(absSender, event, message);
                gameFacade.nextGameActions(absSender, event, message);
                break;
            case WAITING_WITH_GAME:
                gameService.applyGameChecking(event);
                gameFacade.finishGameActions(absSender, event, message);
                break;
            case FINISH_CHECKING:
                eventFacade.finishEventActions(absSender, event, message);
                break;
            default:
        }
    }
}

