package ru.acuma.shuffler.service.game;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.enums.Constants;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChampionshipService {

    private final EventStateService eventStateService;
    private final EventContext eventContext;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final MaintenanceService maintenanceService;

    @SneakyThrows
    public void finishEvent(TgEvent event) {
        eventStateService.cancelled(event);
        event.setFinishedAt(LocalDateTime.now());
//        eventContext.update(event);

        var update = messageService.buildLobbyMessageUpdate(event);
        executeService.execute(update);

        event.missMessage(event.getBaseMessage());
        maintenanceService.sweepChat(event);
        maintenanceService.sweepEvent(event);

        var deleteMethod = messageService.deleteMessage(event.getChatId(), update.getMessageId());
        executeService.executeLater(deleteMethod, Constants.CANCELLED_MESSAGE_TTL_BEFORE_DELETE);
    }

    @SneakyThrows
    public void finishChampionship(TgEvent event) {
        eventStateService.finished(event);
        maintenanceService.sweepEvent(event);
//        eventContext.update(event);
    }

}
