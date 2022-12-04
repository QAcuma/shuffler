package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.ChampionshipService;
import ru.acuma.shuffler.service.api.EventFacade;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class EventFacadeImpl implements EventFacade {

    private final EventStateService eventStateService;
    private final GameStateService gameStateService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final ChampionshipService championshipService;
    private final GameService gameService;

    @Override
    public void finishEventActions(TgEvent event, Message message) {
        maintenanceService.sweepMessage(message.getChatId(), event.getLatestGame().getMessageId());
        gameService.handleGameCheck(event);
        championshipService.finishChampionship(event);
        var lobbyUpdate = messageService.updateLobbyMessage(event);
        executeService.execute(lobbyUpdate);
    }

    @Override
    public void checkingStateActions(TgEvent event) {
        var lobbyUpdate = messageService.updateMarkup(event, event.getBaseMessage(), MessageType.LOBBY);
        var gameUpdate = messageService.updateMarkup(event, event.getLatestGame().getMessageId(), MessageType.GAME);
        var checkingMessage = messageService.sendMessage(event, MessageType.CHECKING);

        executeService.execute(lobbyUpdate);
        executeService.execute(gameUpdate);
        executeService.execute(checkingMessage);
    }

    @Override
    public BiConsumer<Message, TgEvent> getCheckingConsumer() {
        return (message, event) -> {
            event.cancelFutures();
            eventStateService.active(event);
            gameStateService.active(event.getLatestGame());

            var lobbyUpdate = messageService.updateMarkup(event, event.getBaseMessage(), MessageType.LOBBY);
            var gameUpdate = messageService.updateMarkup(event, event.getLatestGame().getMessageId(), MessageType.GAME);

            executeService.execute(lobbyUpdate);
            executeService.execute(gameUpdate);
        };
    }

}
