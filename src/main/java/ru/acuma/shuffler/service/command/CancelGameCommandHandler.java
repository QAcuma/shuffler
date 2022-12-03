package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.CancelGameCommand;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.ANY;

@Service
@RequiredArgsConstructor
public class CancelGameCommandHandler extends CommandHandler<CancelGameCommand> {

    private final GameStateService gameStateService;
    private final EventStateService eventStateService;
    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final MessageService messageService;

    @Override
    public void init() {
        commandExecutorFactory.register(ANY, getCommandClass(), getAnyConsumer());
    }

    @Override
    public Class<CancelGameCommand> getCommandClass() {
        return CancelGameCommand.class;
    }

    @Override
    public void handle(Message message) {
        super.handle(message);
    }

    private BiConsumer<Message, TgEvent> getAnyConsumer() {
        return (message, event) -> {
            gameStateService.cancelCheck(event.getLatestGame());
            eventStateService.cancel(event);

            var lobbyUpdate = messageService.updateLobbyMessage(event);
            var gameUpdate = messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME);
            var checkingMessage = messageService.sendMessage(event, MessageType.CHECKING);

            executeService.execute(lobbyUpdate);
            executeService.execute(gameUpdate);
            executeService.execute(checkingMessage);
        };
    }

}
