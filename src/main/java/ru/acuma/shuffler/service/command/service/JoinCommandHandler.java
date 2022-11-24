package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.api.PlayerService;
import ru.acuma.shuffler.service.aspect.CheckPlayerNotInEvent;
import ru.acuma.shuffler.service.command.JoinCommand;
import ru.acuma.shuffler.service.command.facade.GameFacade;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.CREATED;
import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.READY;
import static ru.acuma.shuffler.model.enums.EventState.WAITING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class JoinCommandHandler extends CommandHandler<JoinCommand> {

    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final GameFacade gameFacade;
    private final EventStateService eventStateService;
    private final PlayerService playerService;

    @Override
    public void init() {
        commandExecutorFactory.register(CREATED, getCommandClass(), getCreatedReadyConsumer());
        commandExecutorFactory.register(READY, getCommandClass(), getCreatedReadyConsumer());
        commandExecutorFactory.register(PLAYING, getCommandClass(), getPlayingConsumer());
        commandExecutorFactory.register(WAITING, getCommandClass(), getWaitingConsumer());
        commandExecutorFactory.register(WAITING_WITH_GAME, getCommandClass(), getWaitingWithGameConsumer());
    }

    @Override
    public Class<JoinCommand> getCommandClass() {
        return JoinCommand.class;
    }

    @Override
    @CheckPlayerNotInEvent
    public void handle(Message message) {
        super.handle(message);
    }


    private BiConsumer<Message, TgEvent> getCreatedReadyConsumer() {
        return (message, event) -> {
            playerService.authenticate(event, message.getFrom());
            eventStateService.definePreparingState(event);
            executeService.execute(messageService.updateMessage(event, event.getBaseMessage(), MessageType.LOBBY));
        };
    }

    private BiConsumer<Message, TgEvent> getPlayingConsumer() {
        return (message, event) -> {
            playerService.joinLobby(event, message.getFrom());
            executeService.execute(messageService.updateLobbyMessage(event));
        };
    }

    private BiConsumer<Message, TgEvent> getWaitingConsumer() {
        return (message, event) -> {
            playerService.joinLobby(event, message.getFrom());
            eventStateService.defineActiveState(event);
            executeService.execute(messageService.updateLobbyMessage(event));
            gameFacade.nextGameActions(event, message);
        };

    }

    private BiConsumer<Message, TgEvent> getWaitingWithGameConsumer() {
        return (message, event) -> {
            playerService.joinLobby(event, message.getFrom());
            eventStateService.defineActiveState(event);
            executeService.execute(messageService.updateLobbyMessage(event));
        };
    }
}
