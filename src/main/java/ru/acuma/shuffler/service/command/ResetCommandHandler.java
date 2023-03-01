package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContext;
import ru.acuma.shuffler.controller.ResetCommand;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.aspect.CheckUserIsAdmin;
import ru.acuma.shuffler.service.aspect.SweepMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.EVICTING;

@Service
@RequiredArgsConstructor
public class ResetCommandHandler extends BaseCommandHandler<ResetCommand> {

    private final EventStateService eventStateService;
    private final EventContext eventContext;
    private final MaintenanceService maintenanceService;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(EVICTING);
    }

    @Override
    @SweepMessage
    @CheckUserIsAdmin
    public void handle(Message message) {
    }

    private BiConsumer<Message, TgEvent> getEvictingConsumer() {
        return (message, event) -> {
            eventStateService.cancelled(event);
            event.setFinishedAt(LocalDateTime.now());
            eventContext.update(event);
            maintenanceService.sweepChat(event);
            maintenanceService.sweepEvent(event);
        };
    }
}
