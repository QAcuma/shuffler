package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.CancelEvictCommand;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.aspect.marker.CheckPlayerInEvent;
import ru.acuma.shuffler.aspect.marker.SweepMessage;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventState.EVICTING;

@Service
@RequiredArgsConstructor
public class CancelEvictCommandHandler extends BaseCommandHandler<CancelEvictCommand> {

    private final EventFacade eventFacade;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(EVICTING);
    }

    @Override
    @SweepMessage
    @CheckPlayerInEvent
    public void handle(Message message, String... args) {
    }

    private BiConsumer<Message, TgEvent> getEvictingConsumer() {
        return eventFacade.getCheckingConsumer();
    }
}
