package ru.acuma.shuffler.service.game;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.service.api.ChampionshipService;
import ru.acuma.shuffler.service.api.EventContextService;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChampionshipServiceImpl implements ChampionshipService {

    private final EventStateService eventStateService;
    private final EventContextService eventContextService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final MaintenanceService maintenanceService;

    @SneakyThrows
    @Override
    public void finishEvent(TgEvent event) {
        eventStateService.cancelledState(event);
        event.setFinishedAt(LocalDateTime.now());
        eventContextService.update(event);

        var update = messageService.updateLobbyMessage(event);
        executeService.execute(update);

        event.missMessage(event.getBaseMessage());
        maintenanceService.sweepChat(event);
        maintenanceService.sweepEvent(event);

        var deleteMethod = messageService.deleteMessage(event.getChatId(), update.getMessageId());
        executeService.executeLater(deleteMethod, Values.CANCELLED_MESSAGE_TIMEOUT);
    }

    @SneakyThrows
    @Override
    public void finishChampionship(TgEvent event) {
        eventStateService.finishedState(event);
        maintenanceService.sweepEvent(event);
        eventContextService.update(event);
    }

}
