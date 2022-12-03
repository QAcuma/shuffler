package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.controller.ResetCommand;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.aspect.CheckUserIsAdmin;
import ru.acuma.shuffler.service.aspect.SweepMessage;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;

import java.time.LocalDateTime;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.EVICTING;

@Service
@RequiredArgsConstructor
public class ResetCommandHandler extends CommandHandler<ResetCommand> {

    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final EventStateService eventStateService;
    private final EventContextServiceImpl eventContextService;
    private final MaintenanceService maintenanceService;

    @Override
    protected void init() {
        commandExecutorFactory.register(EVICTING, getCommandClass(), getEvictingConsumer());
    }

    @Override
    public Class<ResetCommand> getCommandClass() {
        return ResetCommand.class;
    }

    @Override
    @SweepMessage
    @CheckUserIsAdmin
    public void handle(Message message) {
        super.handle(message);
    }

    private BiConsumer<Message, TgEvent> getEvictingConsumer() {
        return (message, event) -> {
            eventStateService.cancelled(event);
            event.setFinishedAt(LocalDateTime.now());
            eventContextService.update(event);
            maintenanceService.sweepChat(event);
            maintenanceService.sweepEvent(event);
        };
    }
}
