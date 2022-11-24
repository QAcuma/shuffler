package ru.acuma.shuffler.service.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.service.command.BaseBotCommand;

import java.util.Optional;
import java.util.function.Consumer;

import static ru.acuma.shuffler.model.enums.EventState.ANY;

@Service
@RequiredArgsConstructor
public class ExecutorService implements Executor {

    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final EventContextServiceImpl eventContextService;

    @Override
    public <T extends BaseBotCommand> void doExecute(Message message, Class<T> commandClass) {
        Consumer<TgEvent> eventExecutor = event -> {
            commandExecutorFactory.getExecutor(event.getEventState(), commandClass)
                    .accept(message, event);
        };
        Runnable emptyExecutor = () -> {
            commandExecutorFactory.getExecutor(ANY, commandClass)
                    .accept(message, null);
        };

        Optional.ofNullable(eventContextService.getCurrentEvent(message.getChatId()))
                .ifPresentOrElse(eventExecutor, emptyExecutor);
    }

}
