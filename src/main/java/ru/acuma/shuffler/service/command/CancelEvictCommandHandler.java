package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.CancelEvictCommand;
import ru.acuma.shuffler.model.dto.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.service.aspect.CheckPlayerInEvent;
import ru.acuma.shuffler.service.aspect.SweepMessage;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.EVICTING;

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
    public void handle(Message message) {
    }

    private BiConsumer<Message, TgEvent> getEvictingConsumer() {
        return eventFacade.getCheckingConsumer();
    }
}
