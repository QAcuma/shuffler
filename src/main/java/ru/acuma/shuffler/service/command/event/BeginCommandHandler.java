package ru.acuma.shuffler.service.command.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.controller.BeginCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.command.common.BaseCommandHandler;
import ru.acuma.shuffler.service.event.EventStatusService;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.READY;

@Service
@RequiredArgsConstructor
public class BeginCommandHandler extends BaseCommandHandler<BeginCommand> {

    private final EventStatusService eventStatusService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(READY);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        eventStatusService.beginChecking(event);
        renderContext.forEvent(event).render(Render.forUpdate(MessageType.LOBBY));
    }
}
