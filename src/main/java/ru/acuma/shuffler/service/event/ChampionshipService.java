package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.Render;
import ru.acuma.shuffler.service.message.MaintenanceService;
import ru.acuma.shuffler.service.message.MessageService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChampionshipService {

    private final EventStatusService eventStatusService;
    private final EventContext eventContext;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;

    @SneakyThrows
    public void finishEvent(TEvent event) {
        eventStatusService.cancelled(event);
        event.setFinishedAt(LocalDateTime.now());

        event.render(Render.forUpdate(MessageType.LOBBY, event.getMessageId(MessageType.LOBBY)));
    }

    @SneakyThrows
    public void finishChampionship(TEvent event) {
        eventStatusService.finished(event);
//        eventContext.update(event);
    }

}
