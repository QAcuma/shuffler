package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.service.ChampionshipService;
import ru.acuma.shuffler.service.EventStateService;
import ru.acuma.shuffler.service.ExecuteService;
import ru.acuma.shuffler.service.MaintenanceService;
import ru.acuma.shuffler.service.MessageService;
import ru.acuma.shuffler.model.enums.Values;

@Service
@RequiredArgsConstructor
public class ChampionshipServiceImpl implements ChampionshipService {

    private final EventStateService eventStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final MaintenanceService maintenanceService;

    @SneakyThrows
    @Override
    public void cancelChampionship(AbsSender absSender, GameEvent event) {
        eventStateService.cancelledState(event);

        var update = messageService.updateLobbyMessage(event);
        executeService.execute(absSender, update);

        event.missMessage(event.getBaseMessage());
        maintenanceService.sweepChat(absSender, event);
        maintenanceService.sweepEvent(event, false);

        executeService.executeLater(absSender,
                () -> maintenanceService.sweepMessage(absSender, Long.valueOf(update.getChatId()), update.getMessageId()),
                Values.CANCELLED_MESSAGE_TIMEOUT
        );
    }

    @SneakyThrows
    @Override
    public void finishChampionship(AbsSender absSender, GameEvent event) {
        eventStateService.finishedState(event);
        maintenanceService.sweepEvent(event, true);
    }

}
