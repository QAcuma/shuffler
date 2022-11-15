package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.api.PlayerService;
import ru.acuma.shuffler.service.aspect.CheckPlayerInEvent;
import ru.acuma.shuffler.service.command.LeaveCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.CREATED;
import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.READY;
import static ru.acuma.shuffler.model.enums.EventState.WAITING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class LeaveCommandHandler extends CommandHandler<LeaveCommand> {

    private final CommandExecutorFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final EventStateService eventStateService;
    private final PlayerService playerService;

    @Override
    public void init() {
        commandExecutorFactory.register(CREATED, getCommandClass(), getCreatedReadyConsumer());
        commandExecutorFactory.register(READY, getCommandClass(), getCreatedReadyConsumer());
        commandExecutorFactory.register(PLAYING, getCommandClass(), getPlayingWaitingWaitingWIthGameConsumer());
        commandExecutorFactory.register(WAITING, getCommandClass(), getPlayingWaitingWaitingWIthGameConsumer());
        commandExecutorFactory.register(WAITING_WITH_GAME, getCommandClass(), getPlayingWaitingWaitingWIthGameConsumer());
    }

    @Override
    public Class<LeaveCommand> getCommandClass() {
        return LeaveCommand.class;
    }

    @Override
    @CheckPlayerInEvent
    public void handle(Message message) {
        super.handle(message);
    }


    private BiConsumer<Message, TgEvent> getCreatedReadyConsumer() {
        return (message, event) -> {
            playerService.leaveLobby(event, message.getFrom().getId());
            eventStateService.definePreparingState(event);
            executeService.execute(messageService.updateLobbyMessage(event));
        };
    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWaitingWIthGameConsumer() {
        return (message, event) -> {
            playerService.leaveLobby(event, message.getFrom().getId());
            eventStateService.defineActiveState(event);
            executeService.execute(messageService.updateLobbyMessage(event));
        };
    }
}
