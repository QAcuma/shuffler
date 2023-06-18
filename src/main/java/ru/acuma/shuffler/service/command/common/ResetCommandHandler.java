package ru.acuma.shuffler.service.command.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.aspect.marker.CheckUserIsAdmin;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.controller.ResetCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.message.MaintenanceService;
import ru.acuma.shuffler.util.TimeMachine;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.ANY;

@Service
@RequiredArgsConstructor
public class ResetCommandHandler extends BaseCommandHandler<ResetCommand> {

    private final EventStatusService eventStatusService;
    private final MaintenanceService maintenanceService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(ANY);
    }

    @Override
    @SweepMessage
    @CheckUserIsAdmin
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
    }

    private BiConsumer<Message, TEvent> getEvictingConsumer() {
        return (message, event) -> {
            eventStatusService.cancelled(event);
            event.setFinishedAt(TimeMachine.localDateTimeNow());
//            eventContext.update(event);
            maintenanceService.sweepChat(event);
        };
    }
}
