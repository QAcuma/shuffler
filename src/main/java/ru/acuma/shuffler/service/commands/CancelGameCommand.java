package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;

@Component
public class CancelGameCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final GameStateService gameStateService;
    private final ExecuteService executeService;
    private final MessageService messageService;

    public CancelGameCommand(EventContextServiceImpl eventContextService, GameStateService gameStateService, ExecuteService executeService, MessageService messageService) {
        super(Command.CANCEL_GAME.getCommand(), "Отменить игру");
        this.eventContextService = eventContextService;
        this.gameStateService = gameStateService;
        this.executeService = executeService;
        this.messageService = messageService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());
        var gameState = event.getLastGame().getState();
        if (gameState == GameState.BLUE_CHECKING || gameState == GameState.RED_CHECKING || gameState == GameState.CANCEL_CHECKING) {
            return;
        }

        gameStateService.cancelCheckingState(event.getLastGame());
        executeService.execute(absSender, messageService.updateMessage(event, event.getLastGame().getMessageId(), MessageType.GAME));
        executeService.execute(absSender, messageService.sendMessage(event, MessageType.CHECKING));
    }
}

