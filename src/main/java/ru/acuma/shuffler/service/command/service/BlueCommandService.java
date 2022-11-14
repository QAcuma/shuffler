package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventContextService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.command.BlueCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class BlueCommandService extends CommandService<BlueCommand> {

    private final EventContextService eventContextService;
    private final GameStateService gameStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final CommandExecutorFactory commandExecutorFactory;

    @Override
    protected void init() {
        commandExecutorFactory.register(PLAYING, getCommandClass(), getPlayingWaitingWithGameConsumer());
        commandExecutorFactory.register(WAITING_WITH_GAME, getCommandClass(), getPlayingWaitingWithGameConsumer());
    }

    @Override
    public Class<BlueCommand> getCommandClass() {
        return BlueCommand.class;
    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWithGameConsumer() {
        return (message, event) -> {
            gameStateService.blueCheckingState(event.getLatestGame());
            var lobbyMessage = messageService.updateLobbyMessage(event);
            executeService.execute(lobbyMessage);
            var gameMessage = messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME);
            executeService.execute(gameMessage);
            var checkingMessage = messageService.sendMessage(event, MessageType.CHECKING);
            executeService.execute(checkingMessage);
        };
    }
}
