package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.NoCommand;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.aspect.SweepMessage;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.EVICTING;
import static ru.acuma.shuffler.model.enums.EventState.FINISH_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.GAME_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING;

@Service
@RequiredArgsConstructor
public class NoCommandHandler extends BaseCommandHandler<NoCommand> {

    private final ExecuteService executeService;
    private final MessageService messageService;
    private final EventStateService eventStateService;
    private final EventFacade eventFacade;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(CANCEL_CHECKING, BEGIN_CHECKING, GAME_CHECKING, EVICTING, WAITING, FINISH_CHECKING);
    }

    @Override
    @SweepMessage
    public void handle(Message message) {
    }

    private BiConsumer<Message, TgEvent> getCancelBeginCheckingConsumer() {
        return (message, event) -> {
            event.cancelFutures();
            eventStateService.prepare(event);
            var lobbyMessage = messageService.buildLobbyMessageUpdate(event);
            executeService.execute(lobbyMessage);
        };
    }

    private BiConsumer<Message, TgEvent> getCheckingConsumer() {
        return eventFacade.getCheckingConsumer();
    }

}