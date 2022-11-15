package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.command.FinishCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class FinishCommandHandler extends CommandHandler<FinishCommand> {

    private final EventStateService eventStateService;
    private final CommandExecutorFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final MessageService messageService;

    @Override
    public void init() {
        commandExecutorFactory.register(PLAYING, getCommandClass(), getPlayingWaitingWithGameConsumer());
        commandExecutorFactory.register(WAITING_WITH_GAME, getCommandClass(), getPlayingWaitingWithGameConsumer());
    }

    @Override
    public Class<FinishCommand> getCommandClass() {
        return FinishCommand.class;
    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWithGameConsumer() {
        return (message, event) -> {
            eventStateService.finishCheckState(event);
            var gameMessage = messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME);
            executeService.execute(gameMessage);
            var lobbyMessage = messageService.updateLobbyMessage(event);
            executeService.execute(lobbyMessage);
            var checkingMessage = messageService.sendMessage(event, MessageType.CHECKING_TIMED);
            executeService.executeSequence(checkingMessage, event);
        };
    }

}
