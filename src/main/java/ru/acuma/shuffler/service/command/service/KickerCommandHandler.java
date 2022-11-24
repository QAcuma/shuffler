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
import ru.acuma.shuffler.service.command.BaseBotCommand;
import ru.acuma.shuffler.service.command.KickerCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;
import ru.acuma.shuffler.service.executor.Executor;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.ANY;

@Service
@RequiredArgsConstructor
public class KickerCommandHandler extends CommandHandler<KickerCommand> {

    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final EventContextServiceImpl eventContextService;

    private final Executor executor = new Executor() {
        @Override
        public <T extends BaseBotCommand> void doExecute(Message message, Class<T> commandClass) {
            getAnyConsumer().accept(message, null);
        }
    };

    @Override
    protected void init() {
        commandExecutorFactory.register(ANY, getCommandClass(), getAnyConsumer());
    }

    @Override
    public Class<KickerCommand> getCommandClass() {
        return KickerCommand.class;
    }

    @Override
    @SweepMessage
    @CheckNoActiveEvent
    public void handle(Message message) {
        executor.doExecute(message, KickerCommand.class);
    }

    private BiConsumer<Message, TgEvent> getAnyConsumer() {
        return (message, event) -> {
            event = eventContextService.buildEvent(message.getChatId(), Discipline.KICKER);
            var baseMessage = executeService.execute(messageService.sendMessage(event, MessageType.LOBBY));
            executeService.execute(messageService.pinMessage(baseMessage));
            event.watchMessage(baseMessage.getMessageId());
        };
    }
}
