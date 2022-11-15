package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.aspect.CheckNoActiveEvent;
import ru.acuma.shuffler.service.aspect.SweepMessage;
import ru.acuma.shuffler.service.command.PingPongCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorFactory;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.ANY;

@Service
@RequiredArgsConstructor
public class PingPongCommandHandler extends CommandHandler<PingPongCommand> {

    private final CommandExecutorFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final EventContextServiceImpl eventContextService;


    @Override
    protected void init() {
        commandExecutorFactory.register(ANY, getCommandClass(), getAnyConsumer());
    }

    @Override
    public Class<PingPongCommand> getCommandClass() {
        return PingPongCommand.class;
    }

    @Override
    @SweepMessage
    @CheckNoActiveEvent
    public void handle(Message message) {
        super.handle(message);
    }

    private BiConsumer<Message, TgEvent> getAnyConsumer() {
        return (message, event) -> {
            event = eventContextService.buildEvent(message.getChatId(), Discipline.PING_PONG);
            var baseMessage = executeService.execute(messageService.sendMessage(event, MessageType.LOBBY));
            executeService.execute(messageService.pinMessage(baseMessage));
            event.watchMessage(baseMessage.getMessageId());
        };
    }
}
