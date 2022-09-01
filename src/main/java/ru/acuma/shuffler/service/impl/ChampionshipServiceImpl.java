package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.service.api.ChampionshipService;
import ru.acuma.shuffler.service.api.EventContextService;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

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
    public void cancelChampionship(AbsSender absSender, TgEvent event) {
        eventStateService.cancelledState(event);
        eventContextService.update(event);

        var update = messageService.updateLobbyMessage(event);
        executeService.execute(absSender, update);

        event.missMessage(event.getBaseMessage());
        maintenanceService.sweepChat(absSender, event);
        maintenanceService.sweepEvent(event, false);

        executeService.executeLater(absSender,
                () -> maintenanceService.sweepMessage(
                        absSender,
                        Long.valueOf(update.getChatId()),
                        update.getMessageId()
                ),
                Values.CANCELLED_MESSAGE_TIMEOUT
        );
    }

    @SneakyThrows
    @Override
    public void finishChampionship(AbsSender absSender, TgEvent event) {
        eventStateService.finishedState(event);
        maintenanceService.sweepEvent(event, true);
        eventContextService.update(event);
    }

}
