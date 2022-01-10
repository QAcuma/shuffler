package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.ChampionshipService;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.GameService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;
import ru.acuma.k.shuffler.service.PlayerService;

import static ru.acuma.k.shuffler.model.enums.WinnerState.BLUE;
import static ru.acuma.k.shuffler.model.enums.WinnerState.NONE;
import static ru.acuma.k.shuffler.model.enums.WinnerState.RED;
import static ru.acuma.k.shuffler.model.enums.messages.MessageType.GAME;

@Component
public class YesCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final ChampionshipService championshipService;
    private final GameService gameService;
    private final PlayerService playerService;

    public YesCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService, ChampionshipService championshipService, GameService gameService, PlayerService playerService) {
        super(Command.YES.getCommand(), "Да");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
        this.championshipService = championshipService;
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getEvent(message.getChatId());
        switch (event.getEventState()) {
            case CANCEL_LOBBY_CHECKING:
                championshipService.cancelChampionship(absSender, event);
                break;
            case BEGIN_CHECKING:
                gameAnswer(absSender, event, message);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case RED_CHECKING:
                gameService.endGame(event, RED);
                maintenanceService.sweepMessage(absSender, message.getChatId(), event.getCurrentGame().getMessageId());
                gameAnswer(absSender, event, message);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case BLUE_CHECKING:
                gameService.endGame(event, BLUE);
                maintenanceService.sweepMessage(absSender, message.getChatId(), event.getCurrentGame().getMessageId());
                gameAnswer(absSender, event, message);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case CANCEL_GAME_CHECKING:
                gameService.endGame(event, NONE);
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
    private void gameAnswer(AbsSender absSender, KickerEvent event, Message message) {
        maintenanceService.sweepMessage(absSender, message);
        eventStateService.playingState(event);
        event.newGame(gameService.buildGame(event));
        var gameMessage = executeService.execute(absSender, messageService.sendMessage(event, GAME));
        event.getCurrentGame().setMessageId(gameMessage.getMessageId());
    }

    @SneakyThrows
    private void finishAnswer(AbsSender absSender, KickerEvent event, Message message) {
        maintenanceService.sweepMessage(absSender, message);
        maintenanceService.sweepMessage(absSender, message.getChatId(), event.getCurrentGame().getMessageId());
        gameService.endGame(event, NONE);
        championshipService.finishChampionship(absSender, event);
        executeService.execute(absSender, messageService.updateLobbyMessage(event));
    }
}

