package ru.acuma.shuffler.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
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
    public void finishEventActions(TgEvent event, Message message) {
        maintenanceService.sweepMessage(message.getChatId(), event.getLatestGame().getMessageId());
        gameService.applyGameChecking(event);
        championshipService.finishChampionship(event);
        executeService.execute(messageService.updateLobbyMessage(event));
    }

}
