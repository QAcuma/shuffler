package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.NoCommand;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventFacade;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.aspect.SweepMessage;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.EVICTING;
import static ru.acuma.shuffler.model.enums.EventState.FINISH_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.GAME_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING;

@Service
@RequiredArgsConstructor
public class NoCommandHandler extends CommandHandler<NoCommand> {

    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final EventStateService eventStateService;
    private final GameStateService gameStateService;
    private final EventFacade eventFacade;

    @Override
    public void init() {
        commandExecutorFactory.register(CANCEL_CHECKING, getCommandClass(), getCancelBeginCheckingConsumer());
        commandExecutorFactory.register(BEGIN_CHECKING, getCommandClass(), getCancelBeginCheckingConsumer());
        commandExecutorFactory.register(GAME_CHECKING, getCommandClass(), getCheckingConsumer());
        commandExecutorFactory.register(EVICTING, getCommandClass(), getCheckingConsumer());
        commandExecutorFactory.register(WAITING, getCommandClass(), getWaitingFinishCheckingConsumer());
        commandExecutorFactory.register(FINISH_CHECKING, getCommandClass(), getWaitingFinishCheckingConsumer());
    }

    @Override
    public Class<NoCommand> getCommandClass() {
        return NoCommand.class;
    }

    @Override
    @SweepMessage
    public void handle(Message message) {
        super.handle(message);
    }

    private BiConsumer<Message, TgEvent> getWaitingFinishCheckingConsumer() {
        return (message, event) -> {
            eventStateService.active(event);
            gameStateService.active(event.getLatestGame());

            var lobbyMessage = messageService.updateLobbyMessage(event);
            var gameMessage = messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME);

            eventStateService.active(event);
            executeService.execute(lobbyMessage);
            executeService.execute(gameMessage);
        };
    }

    private BiConsumer<Message, TgEvent> getCancelBeginCheckingConsumer() {
        return (message, event) -> {
            eventStateService.prepare(event);
            var lobbyMessage = messageService.updateLobbyMessage(event);
            executeService.execute(lobbyMessage);
        };
    }

    private BiConsumer<Message, TgEvent> getCheckingConsumer() {
        return eventFacade.getCheckingConsumer();
    }

}
