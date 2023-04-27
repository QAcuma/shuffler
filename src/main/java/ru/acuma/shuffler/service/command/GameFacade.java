package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.message.MaintenanceService;

@Service
@RequiredArgsConstructor
public class GameFacade {

    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;
    private final GameService gameService;

    @SneakyThrows
    @Transactional
    public void finishGameActions(TgEvent event, Message message) {
        maintenanceService.sweepMessage(message.getChatId(), event.getLatestGame().getMessageId());
        eventStateService.active(event);
//        var lobbyMessage = messageService.buildLobbyMessageUpdate(event);
//        executeService.execute(lobbyMessage);
    }

    @SneakyThrows
    @Transactional
    public void nextGameActions(TgEvent event, Message message) {
        if (event.getEventState() == EventState.WAITING) {
            return;
        }
        eventStateService.active(event);
        gameService.nextGame(event);

//        var method = messageService.buildMessage(event, MessageType.GAME);
//        var gameMessage = executeService.execute(method);
//        event.getLatestGame().setMessageId(gameMessage.getMessageId());
    }
}
