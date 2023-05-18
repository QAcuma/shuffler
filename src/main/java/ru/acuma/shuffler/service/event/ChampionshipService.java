package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.context.RenderContext;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.context.Render;
import ru.acuma.shuffler.service.message.MaintenanceService;
import ru.acuma.shuffler.service.message.TelegramMethodService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChampionshipService {

    private final EventStatusService eventStatusService;
    private final RenderContext renderContext;
    private final MaintenanceService maintenanceService;
    private final TelegramMethodService telegramMethodService;

    @SneakyThrows
    public void finishEvent(TEvent event) {
        eventStatusService.cancelled(event);
        event.setFinishedAt(LocalDateTime.now());

        renderContext.forChat(event.getChatId()).render(Render.forUpdate(MessageType.LOBBY));
    }

    @SneakyThrows
    public void finishChampionship(TEvent event) {
        eventStatusService.finished(event);
//        eventContext.update(event);
    }

}
