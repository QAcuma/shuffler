package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.WinnerState;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.ChampionshipService;
import ru.acuma.shuffler.service.EventStateService;
import ru.acuma.shuffler.service.ExecuteService;
import ru.acuma.shuffler.service.GameService;
import ru.acuma.shuffler.service.MaintenanceService;
import ru.acuma.shuffler.service.MessageService;

@Component
public class YesCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final ChampionshipService championshipService;
    private final GameService gameService;

    public YesCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService, ChampionshipService championshipService, GameService gameService) {
        super(Command.YES.getCommand(), "Да");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
        this.championshipService = championshipService;
        this.gameService = gameService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());
        switch (event.getEventState()) {
            case CANCEL_LOBBY_CHECKING:
                championshipService.cancelChampionship(absSender, event);
                break;
            case BEGIN_CHECKING:
                gameAnswer(absSender, event, message);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case RED_CHECKING:
                gameService.endGame(event, WinnerState.RED);
                maintenanceService.sweepMessage(absSender, message.getChatId(), event.getCurrentGame().getMessageId());
                gameAnswer(absSender, event, message);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case BLUE_CHECKING:
                gameService.endGame(event, WinnerState.BLUE);
                maintenanceService.sweepMessage(absSender, message.getChatId(), event.getCurrentGame().getMessageId());
                gameAnswer(absSender, event, message);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case CANCEL_GAME_CHECKING:
                gameService.endGame(event, WinnerState.NONE);
                maintenanceService.sweepMessage(absSender, message.getChatId(), event.getCurrentGame().getMessageId());
                gameAnswer(absSender, event, message);
                break;
            case FINISH_CHECKING:
                finishAnswer(absSender, event, message);
                break;
            default:
        }
    }

    @SneakyThrows
    private void gameAnswer(AbsSender absSender, TgEvent event, Message message) {
        maintenanceService.sweepMessage(absSender, message);
        eventStateService.playingState(event);
        event.newGame(gameService.buildGame(event));
        var gameMessage = executeService.execute(absSender, messageService.sendMessage(event, MessageType.GAME));
        event.getCurrentGame().setMessageId(gameMessage.getMessageId());
    }

    @SneakyThrows
    private void finishAnswer(AbsSender absSender, TgEvent event, Message message) {
        maintenanceService.sweepMessage(absSender, message);
        maintenanceService.sweepMessage(absSender, message.getChatId(), event.getCurrentGame().getMessageId());
        gameService.endGame(event, WinnerState.NONE);
        championshipService.finishChampionship(absSender, event);
        executeService.execute(absSender, messageService.updateLobbyMessage(event));
    }
}

