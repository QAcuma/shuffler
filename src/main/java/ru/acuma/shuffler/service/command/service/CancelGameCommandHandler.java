package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.command.CancelGameCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.ANY;

@Service
@RequiredArgsConstructor
public class CancelGameCommandHandler extends CommandHandler<CancelGameCommand> {

    private final GameStateService gameStateService;
    private final CommandExecutorFactory commandExecutorFactory;
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
            gameStateService.cancelCheckingState(event.getLatestGame());
            executeService.execute(messageService.updateLobbyMessage(event));
            executeService.execute(messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME));
            executeService.execute(messageService.sendMessage(event, MessageType.CHECKING));
        };
    }

}
