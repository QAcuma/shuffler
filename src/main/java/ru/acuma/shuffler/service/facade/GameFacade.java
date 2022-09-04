package ru.acuma.shuffler.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

@Service
@RequiredArgsConstructor
public class GameFacade {

    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final GameService gameService;

    @SneakyThrows
    @Transactional
    public void finishGameActions(AbsSender absSender, TgEvent event, Message message) {
        maintenanceService.sweepMessage(absSender, message.getChatId(), event.getLatestGame().getMessageId());
        eventStateService.defineActiveState(event);
        executeService.execute(absSender, messageService.updateLobbyMessage(event));
    }

    @SneakyThrows
    @Transactional
    public void nextGameActions(AbsSender absSender, TgEvent event, Message message) {
        if (event.getEventState() == EventState.WAITING) {
            return;
        }
        eventStateService.playingState(event);
        gameService.nextGame(event);

        var method = messageService.sendMessage(event, MessageType.GAME);
        var gameMessage = executeService.execute(absSender, method);
        event.getLatestGame().setMessageId(gameMessage.getMessageId());
    }
}
