package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;

@Component
public class RedCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final GameStateService gameStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public RedCommand(EventContextServiceImpl eventContextService, GameStateService gameStateService, MessageService messageService, ExecuteService executeService) {
        super(Command.RED.getCommand(), "Красные");
        this.eventContextService = eventContextService;
        this.gameStateService = gameStateService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());
        var gameState = event.getLastGame().getState();
        if (gameState == GameState.BLUE_CHECKING || gameState == GameState.RED_CHECKING || gameState == GameState.CANCEL_CHECKING) {
            return;
        }

        gameStateService.redCheckingState(event.getLastGame());
        executeService.execute(absSender, messageService.updateMessage(event, event.getLastGame().getMessageId(), MessageType.GAME));
        executeService.execute(absSender, messageService.sendMessage(event, MessageType.CHECKING));
    }
}

