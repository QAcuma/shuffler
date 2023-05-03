package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.controller.NoCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventStatusService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.EVICTING;
import static ru.acuma.shuffler.model.constant.EventStatus.FINISH_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.GAME_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING;

@Service
@RequiredArgsConstructor
public class NoCommandHandler extends BaseCommandHandler<NoCommand> {

    private final EventStatusService eventStateService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(CANCEL_CHECKING, BEGIN_CHECKING, GAME_CHECKING, EVICTING, WAITING, FINISH_CHECKING);
    }


    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
    }

    private BiConsumer<Message, TEvent> getCancelBeginCheckingConsumer() {
        return (message, event) -> {
            event.cancelFutures();
            eventStateService.prepare(event);
//            var lobbyMessage = messageService.buildLobbyMessageUpdate(event);
//            executeService.execute(lobbyMessage);
        };
    }
}
