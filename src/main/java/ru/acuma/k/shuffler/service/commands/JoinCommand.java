package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.GameService;
import ru.acuma.k.shuffler.service.MessageService;
import ru.acuma.k.shuffler.service.PlayerService;

import static ru.acuma.k.shuffler.model.enums.Values.GAME_PLAYERS_COUNT;
import static ru.acuma.k.shuffler.model.enums.messages.MessageType.GAME;
import static ru.acuma.k.shuffler.model.enums.messages.MessageType.LOBBY;

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
                executeService.execute(absSender, messageService.updateMessage(event, event.getBaseMessage(), LOBBY));
                break;
            case WAITING:
                playerService.joinLobby(event, message.getFrom());
                if (event.getActivePlayers().size() >= GAME_PLAYERS_COUNT) {
                    eventStateService.playingState(event);
                    event.newGame(gameService.buildGame(event));
                    var gameMessage = executeService.execute(absSender, messageService.sendMessage(event, GAME));
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

