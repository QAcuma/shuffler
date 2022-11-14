package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.command.BeginCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.READY;

@Service
@RequiredArgsConstructor
public class BeginCommandService extends CommandService<BeginCommand> {

    private final CommandExecutorFactory commandExecutorFactory;
    private final EventStateService eventStateService;
    private final ExecuteService executeService;
    private final MessageService messageService;

    @Override
    protected void init() {
        commandExecutorFactory.register(READY, getCommandClass(), getReadyConsumer());
    }

    @Override
    public Class<BeginCommand> getCommandClass() {
        return BeginCommand.class;
    }

    private BiConsumer<Message, TgEvent> getReadyConsumer() {
        return (message, event) -> {
            eventStateService.beginCheckState(event);
            var lobbyMessage = messageService.updateLobbyMessage(event);
            var checkingMessage = messageService.sendMessage(event, MessageType.CHECKING);
            executeService.execute(lobbyMessage);
            executeService.execute(checkingMessage);
        };
    }
}
