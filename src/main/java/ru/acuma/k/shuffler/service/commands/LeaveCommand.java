package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.*;

import static ru.acuma.k.shuffler.model.enums.Values.GAME_PLAYERS_COUNT;

@Component
public class LeaveCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final PlayerService playerService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public LeaveCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, PlayerService playerService, MessageService messageService, ExecuteService executeService) {
        super(Command.LEAVE.getCommand(), "Покинуть список участников");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.playerService = playerService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());
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
                playerService.leaveLobby(event, message.getFrom());
                if (event.getActivePlayers().size() < GAME_PLAYERS_COUNT) {
                    eventStateService.waitingState(event);
                }
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
        }
    }

}

