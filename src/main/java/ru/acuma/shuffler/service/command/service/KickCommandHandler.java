package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.KickService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.aspect.CheckPlayerInEvent;
import ru.acuma.shuffler.service.aspect.SweepMessage;
import ru.acuma.shuffler.service.command.KickCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class KickCommandHandler extends CommandHandler<KickCommand> {

    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final EventStateService eventStateService;
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final KickService kickService;
    private final GameStateService gameStateService;

    @Override
    protected void init() {
        commandExecutorFactory.register(PLAYING, getCommandClass(), getPlayingWaitingWaitingWIthGameConsumer());
        commandExecutorFactory.register(WAITING, getCommandClass(), getPlayingWaitingWaitingWIthGameConsumer());
        commandExecutorFactory.register(WAITING_WITH_GAME, getCommandClass(), getPlayingWaitingWaitingWIthGameConsumer());
    }

    @Override
    public Class<KickCommand> getCommandClass() {
        return KickCommand.class;
    }

    @Override
    @SweepMessage
    @CheckPlayerInEvent
    public void handle(Message message) {
        super.handle(message);
    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWaitingWIthGameConsumer() {
        return (message, event) -> {
            eventStateService.evictingState(event);
            gameStateService.cancelCheckingState(event.getLatestGame());
            var method = kickService.prepareKickMessage(event);

            executeService.execute(method);
            executeService.execute(messageService.updateLobbyMessage(event));
            executeService.execute(messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME));
        };
    }
}
