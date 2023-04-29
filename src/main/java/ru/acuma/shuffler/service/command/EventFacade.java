package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.event.ChampionshipService;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.message.MaintenanceService;

import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class EventFacade {

    private final EventStatusService eventStateService;
    private final GameStateService gameStateService;
    private final MaintenanceService maintenanceService;
    private final ChampionshipService championshipService;
    private final GameService gameService;

    public void finishEventActions(TEvent event, Message message) {
//        maintenanceService.sweepMessage(message.getChatId(), event.getCurrentGame().getMessageId());
//        gameService.handleGameCheck(event);
//        championshipService.finishChampionship(event);
//        var lobbyUpdate = messageService.buildLobbyMessageUpdate(event);
//        executeService.execute(lobbyUpdate);
    }

    public void checkingStateActions(TEvent event) {
//        var lobbyUpdate = messageService.buildReplyMarkupUpdate(event, event.getLobbyMessageId(), MessageType.LOBBY);
//        var gameUpdate = messageService.buildReplyMarkupUpdate(event, event.getLatestGame().getMessageId(), MessageType.GAME);
//        var checkingMessage = messageService.buildMessage(event, MessageType.CHECKING);
//
//        executeService.execute(lobbyUpdate);
//        executeService.execute(gameUpdate);
//        executeService.execute(checkingMessage);
    }

    public BiConsumer<Message, TEvent> getCheckingConsumer() {
        return (message, event) -> {
            event.cancelFutures();
            eventStateService.resume(event);
            gameStateService.active(event.getCurrentGame());

//            var lobbyUpdate = messageService.buildReplyMarkupUpdate(event, event.getLobbyMessageId(), MessageType.LOBBY);
//            var gameUpdate = messageService.buildReplyMarkupUpdate(event, event.getLatestGame().getMessageId(), MessageType.GAME);
//
//            executeService.execute(lobbyUpdate);
//            executeService.execute(gameUpdate);
        };
    }

}
