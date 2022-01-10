package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.GameService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;
import ru.acuma.k.shuffler.service.PlayerService;

import static ru.acuma.k.shuffler.model.enums.Values.GAME_PLAYERS_COUNT;
import static ru.acuma.k.shuffler.model.enums.WinnerState.NONE;
import static ru.acuma.k.shuffler.model.enums.messages.MessageType.GAME;

@Component
public class LeaveCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final PlayerService playerService;
    private final MessageService messageService;
    private final MaintenanceService maintenanceService;
    private final ExecuteService executeService;
    private final GameService gameService;

    public LeaveCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, PlayerService playerService, MessageService messageService, MaintenanceService maintenanceService, ExecuteService executeService, GameService gameService) {
        super(Command.LEAVE.getCommand(), "Покинуть список участников");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.playerService = playerService;
        this.messageService = messageService;
        this.maintenanceService = maintenanceService;
        this.executeService = executeService;
        this.gameService = gameService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getEvent(message.getChatId());
        if (!event.isPresent(message.getFrom().getId())) {
            return;
        }
        switch (event.getEventState()) {
            case CREATED:
            case READY:
                playerService.leaveLobby(event, message.getFrom());
                eventStateService.lobbyState(event);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case PLAYING:
                boolean broken = playerService.leaveLobby(event, message.getFrom());
                if (broken) {
                    gameService.endGame(event, NONE);
                    if (event.getActivePlayers().size() < GAME_PLAYERS_COUNT) {
                        eventStateService.waitingState(event);
                        maintenanceService.sweepMessage(absSender, event.getChatId(), event.getCurrentGame().getMessageId());
                    } else {
                        gameAnswer(absSender, event, event.getCurrentGame().getMessageId());
                    }
                }
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
        }
    }

    @SneakyThrows
    private void gameAnswer(AbsSender absSender, KickerEvent event, Integer messageId) {
        maintenanceService.sweepMessage(absSender, event.getChatId(), messageId);
        eventStateService.playingState(event);
        event.newGame(gameService.buildGame(event));
        var gameMessage = executeService.execute(absSender, messageService.sendMessage(event, GAME));
        event.getCurrentGame().setMessageId(gameMessage.getMessageId());
    }

}

