package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.EvictCommand;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.aspect.marker.CheckPlayerInEvent;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.service.user.PlayerService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventState.EVICTING;

@Service
@RequiredArgsConstructor
public class EvictCommandHandler extends BaseCommandHandler<EvictCommand> {

    private final EventStateService eventStateService;
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final GameFacade gameFacade;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(EVICTING);
    }

    @Override
    @SweepMessage
    @CheckPlayerInEvent
    public void handle(Message message, String... args) {
    }

    private BiConsumer<Message, TgEvent> getEvictingConsumer() {
        return (message, event) -> {
            playerService.leaveLobby(event, Long.valueOf(message.getText()));
            eventStateService.active(event);

            executeService.execute(messageService.buildMessageUpdate(event, event.getBaseMessage(), MessageType.LOBBY));
            gameService.handleGameCheck(event);
            gameFacade.finishGameActions(event, message);

            eventStateService.active(event);
            gameFacade.nextGameActions(event, message);
        };
    }
}
