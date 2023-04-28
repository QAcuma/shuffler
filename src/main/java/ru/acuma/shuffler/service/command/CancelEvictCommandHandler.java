package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.aspect.marker.CheckPlayerInEvent;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.controller.CancelEvictCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.EVICTING;

@Service
@RequiredArgsConstructor
public class CancelEvictCommandHandler extends BaseCommandHandler<CancelEvictCommand> {

    private final EventFacade eventFacade;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(EVICTING);
    }


    @Override
    @SweepMessage
    @CheckPlayerInEvent
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
    }

    private BiConsumer<Message, TEvent> getEvictingConsumer() {
        return eventFacade.getCheckingConsumer();
    }
}
