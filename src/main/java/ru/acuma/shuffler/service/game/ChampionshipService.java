package ru.acuma.shuffler.service.game;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.service.message.Render;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.message.MaintenanceService;
import ru.acuma.shuffler.service.message.MessageService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChampionshipService {

    private final EventStateService eventStateService;
    private final EventContext eventContext;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;

    @SneakyThrows
    public void finishEvent(TgEvent event) {
        eventStateService.cancelled(event);
        event.setFinishedAt(LocalDateTime.now());

        event.action(MessageType.LOBBY, Render.forUpdate(event.getLobbyMessageId()));
    }

    @SneakyThrows
    public void finishChampionship(TgEvent event) {
        eventStateService.finished(event);
        maintenanceService.flushEvent(event);
//        eventContext.update(event);
    }

}
