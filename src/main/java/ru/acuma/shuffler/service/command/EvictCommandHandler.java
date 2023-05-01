package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.aspect.marker.CheckPlayerInEvent;
import ru.acuma.shuffler.controller.EvictCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.telegram.PlayerService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.EVICTING;

@Service
@RequiredArgsConstructor
public class EvictCommandHandler extends BaseCommandHandler<EvictCommand> {

    private final EventStatusService eventStateService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final GameFacade gameFacade;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(EVICTING);
    }

    @Override
    @CheckPlayerInEvent
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
    }

    private BiConsumer<Message, TEvent> getEvictingConsumer() {
        return (message, event) -> {
//            playerService.leaveLobby(event, Long.valueOf(message.getText()));
//            eventStateService.resume(event);
//
////            executeService.execute(messageService.buildMessageUpdate(event, event.getLobbyMessageId(), MessageType.LOBBY));
//            gameService.handleGameCheck(event);
//            gameFacade.finishGameActions(event, message);
//
//            eventStateService.resume(event);
//            gameFacade.nextGameActions(event, message);
        };
    }
}
