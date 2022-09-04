package ru.acuma.shuffler.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.WinnerState;
import ru.acuma.shuffler.service.api.ChampionshipService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

@Service
@RequiredArgsConstructor
public class EventFacade {

    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final ChampionshipService championshipService;
    private final GameService gameService;

    @SneakyThrows
    public void finishEventActions(AbsSender absSender, TgEvent event, Message message) {
        maintenanceService.sweepMessage(absSender, message);
        maintenanceService.sweepMessage(absSender, message.getChatId(), event.getLastGame().getMessageId());
        gameService.applyGameChecking(event);
        championshipService.finishChampionship(absSender, event);
        executeService.execute(absSender, messageService.updateLobbyMessage(event));
    }

}
