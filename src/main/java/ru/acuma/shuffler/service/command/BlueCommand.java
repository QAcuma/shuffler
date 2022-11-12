package ru.acuma.shuffler.service.command;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;

@Component
public class BlueCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final GameStateService gameStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public BlueCommand(EventContextServiceImpl eventContextService, GameStateService gameStateService, MessageService messageService, ExecuteService executeService) {
        super(Command.BLUE.getCommand(), "Синие");
        this.eventContextService = eventContextService;
        this.gameStateService = gameStateService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        var event = eventContextService.getCurrentEvent(message.getChatId());
        var gameState = event.getLatestGame().getState();
        if (gameState.in(GameState.BLUE_CHECKING, GameState.RED_CHECKING, GameState.CANCEL_CHECKING) || event.getEventState().in(EventState.FINISH_CHECKING)) {
            return;
        }

        gameStateService.blueCheckingState(event.getLatestGame());
        executeService.execute(messageService.updateLobbyMessage(event));
        executeService.execute(messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME));
        executeService.execute(messageService.sendMessage(event, MessageType.CHECKING));
    }
}

