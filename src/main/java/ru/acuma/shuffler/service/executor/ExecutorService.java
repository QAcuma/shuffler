package ru.acuma.shuffler.service.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.service.command.BaseBotCommand;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExecutorService implements Executor {

    private final CommandExecutorFactory commandExecutorFactory;
    private final EventContextServiceImpl eventContextService;

    @Override
    public <T extends BaseBotCommand> void doExecute(Message message, Class<T> commandClass) {
        Optional.ofNullable(eventContextService.getCurrentEvent(message.getChatId()))
                .ifPresent(event -> {
                    commandExecutorFactory.getExecutor(event.getEventState(), commandClass)
                            .accept(message, event);
                });
    }

}
