package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.api.ChampionshipService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.executor.ExecutorFactory;
import ru.acuma.shuffler.service.facade.EventFacade;
import ru.acuma.shuffler.service.facade.GameFacade;

import javax.annotation.PostConstruct;

import static ru.acuma.shuffler.model.enums.EventState.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.FINISH_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.PLAYING;

@Component
public class YesCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final ChampionshipService championshipService;
    private final GameService gameService;
    private final GameFacade gameFacade;
    private final EventFacade eventFacade;
    private final ExecutorFactory executorFactory;

    public YesCommand(EventContextServiceImpl eventContextService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService, ChampionshipService championshipService, GameService gameService, GameFacade gameFacade, EventFacade eventFacade, ExecutorFactory executorFactory) {
        super(Command.YES.getCommand(), "Да");
        this.eventContextService = eventContextService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
        this.championshipService = championshipService;
        this.gameService = gameService;
        this.gameFacade = gameFacade;
        this.eventFacade = eventFacade;
        this.executorFactory = executorFactory;
    }

    @PostConstruct
    private void init() {
        executorFactory.register(
                CANCEL_CHECKING,
                this.getClass(),
                (message, event) -> championshipService.finishEvent(event)
        );

        executorFactory.register(
                BEGIN_CHECKING,
                this.getClass(),
                (message, event) -> {
                    gameFacade.nextGameActions(event, message);
                    executeService.execute(messageService.updateLobbyMessage(event));
                }
        );

        executorFactory.register(
                PLAYING,
                this.getClass(),
                (message, event) -> {
                    gameService.applyGameChecking(event);
                    gameFacade.finishGameActions(event, message);
                    gameFacade.nextGameActions(event, message);
                }
        );

        executorFactory.register(
                PLAYING,
                this.getClass(),
                (message, event) -> {
                    gameService.applyGameChecking(event);
                    gameFacade.finishGameActions(event, message);
                }
        );

        executorFactory.register(
                FINISH_CHECKING,
                this.getClass(),
                (message, event) -> eventFacade.finishEventActions(event, message)
        );

    }

    @Override
    @SneakyThrows
    public void execute(Message message) {
        var event = eventContextService.getCurrentEvent(message.getChatId());
        maintenanceService.sweepMessage(message);

        if (event == null) {
            return;
        }

        executorFactory.getExecutor(event.getEventState(), this.getClass()).accept(message, event);
    }
}

