package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.EventStateService;
import ru.acuma.shuffler.service.ExecuteService;
import ru.acuma.shuffler.service.GameService;
import ru.acuma.shuffler.service.MessageService;
import ru.acuma.shuffler.service.PlayerService;

import static ru.acuma.shuffler.model.enums.GameState.STARTED;

@Component
public class JoinCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final PlayerService playerService;
    private final GameService gameService;

    public JoinCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, MessageService messageService, ExecuteService executeService, PlayerService playerService, GameService gameService) {
        super(Command.JOIN.getCommand(), "Присоединиться к игре");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.messageService = messageService;
        this.executeService = executeService;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());
        if (event.isPresent(message.getFrom().getId())) {
            return;
        }
        switch (event.getEventState()) {
            case CREATED:
            case READY:
                playerService.authenticate(event, message.getFrom());
                eventStateService.lobbyState(event);
                executeService.execute(absSender, messageService.updateMessage(event, event.getBaseMessage(), MessageType.LOBBY));
                break;
            case WAITING:
                playerService.joinLobby(event, message.getFrom());
                if (event.getActivePlayers().size() >= Values.GAME_PLAYERS_COUNT) {
                    eventStateService.playingState(event);
                    if (event.getCurrentGame().getState() != STARTED) {
                        event.newGame(gameService.buildGame(event));
                        return;
                    }
                    var gameMessage = executeService.execute(absSender, messageService.sendMessage(event, MessageType.GAME));
                    event.getCurrentGame().setMessageId(gameMessage.getMessageId());
                }
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case PLAYING:
                playerService.joinLobby(event, message.getFrom());
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
        }
    }
}

