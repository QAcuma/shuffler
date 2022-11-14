package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventContextService;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.aspect.SweepMessage;
import ru.acuma.shuffler.service.command.NoCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.FINISH_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class NoCommandService extends CommandService<NoCommand> {

    private final EventContextService eventContextService;
    private final CommandExecutorFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final EventStateService eventStateService;
    private final GameStateService gameStateService;

    @Override
    public void init() {
        commandExecutorFactory.register(CANCEL_CHECKING, getCommandClass(), getCancelBeginCheckingConsumer());
        commandExecutorFactory.register(BEGIN_CHECKING, getCommandClass(), getCancelBeginCheckingConsumer());
        commandExecutorFactory.register(PLAYING, getCommandClass(), getPlayingPlayingWithGameConsumer());
        commandExecutorFactory.register(WAITING_WITH_GAME, getCommandClass(), getPlayingPlayingWithGameConsumer());
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
            eventStateService.defineActiveState(event);
            executeService.execute(messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME));
        };
    }

    private BiConsumer<Message, TgEvent> getCancelBeginCheckingConsumer() {
        return (message, event) -> {
            eventStateService.definePreparingState(event);
            executeService.execute(messageService.updateLobbyMessage(event));
        };
    }

    private BiConsumer<Message, TgEvent> getPlayingPlayingWithGameConsumer() {
        return (message, event) -> {
            gameStateService.activeState(event.getLatestGame());
            eventStateService.defineActiveState(event);
            executeService.execute(messageService.updateLobbyMessage(event));
            executeService.execute(messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME));
        };
    }

}
