package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.service.ChampionshipService;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;

import static ru.acuma.k.shuffler.model.enums.Values.CANCELLED_MESSAGE_TIMEOUT;

@Service
@RequiredArgsConstructor
public class ChampionshipServiceImpl implements ChampionshipService {

    private final EventStateService eventStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final MaintenanceService maintenanceService;

    @SneakyThrows
    @Override
    public void cancelChampionship(AbsSender absSender, KickerEvent event) {
        eventStateService.cancelledState(event);

        var update = messageService.updateLobbyMessage(event);
        executeService.execute(absSender, update);

        event.missMessage(event.getBaseMessage());
        maintenanceService.sweepChat(absSender, event);
        maintenanceService.sweepEvent(event, false);

        executeService.executeLater(absSender,
                () -> maintenanceService.sweepMessage(absSender, Long.valueOf(update.getChatId()), update.getMessageId()),
                CANCELLED_MESSAGE_TIMEOUT
        );
    }

    @SneakyThrows
    @Override
    public void finishChampionship(AbsSender absSender, KickerEvent event) {
        eventStateService.finishedState(event);
        maintenanceService.sweepEvent(event, true);
    }

}
