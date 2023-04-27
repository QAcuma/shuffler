package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.controller.ResetCommand;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.message.MaintenanceService;
import ru.acuma.shuffler.aspect.marker.CheckUserIsAdmin;
import ru.acuma.shuffler.aspect.marker.SweepMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventState.EVICTING;

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
    public void handle(final Message message, final String... args) {
    }

    private BiConsumer<Message, TgEvent> getEvictingConsumer() {
        return (message, event) -> {
            eventStateService.cancelled(event);
            event.setFinishedAt(LocalDateTime.now());
//            eventContext.update(event);
            maintenanceService.sweepChat(event);
            maintenanceService.flushEvent(event);
        };
    }
}
