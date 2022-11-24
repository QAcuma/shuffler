package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.command.CancelCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.CREATED;
import static ru.acuma.shuffler.model.enums.EventState.READY;

@Service
@RequiredArgsConstructor
public class CancelCommandHandler extends CommandHandler<CancelCommand> {

    private final EventStateService eventStateService;
    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final MessageService messageService;

    @Override
    public void init() {
        commandExecutorFactory.register(CREATED, getCommandClass(), getAnyConsumer());
        commandExecutorFactory.register(READY, getCommandClass(), getAnyConsumer());
    }

    @Override
    public Class<CancelCommand> getCommandClass() {
        return CancelCommand.class;
    }

    @Override
    public void handle(Message message) {
        super.handle(message);
    }

    private BiConsumer<Message, TgEvent> getAnyConsumer() {
        return (message, event) -> {
            eventStateService.cancelCheckState(event);
            var lobbyMessage = messageService.updateLobbyMessage(event);
            executeService.execute(lobbyMessage);
            var checkingMessage = messageService.sendMessage(event, MessageType.CHECKING_TIMED);
            executeService.executeSequence(checkingMessage, event);
        };
    }

}
