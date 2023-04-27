package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.aspect.marker.CheckPlayerInEvent;
import ru.acuma.shuffler.controller.LeaveCommand;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.user.PlayerService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventState.CREATED;
import static ru.acuma.shuffler.model.constant.EventState.PLAYING;
import static ru.acuma.shuffler.model.constant.EventState.READY;
import static ru.acuma.shuffler.model.constant.EventState.WAITING;
import static ru.acuma.shuffler.model.constant.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class LeaveCommandHandler extends BaseCommandHandler<LeaveCommand> {

    private final EventStateService eventStateService;
    private final PlayerService playerService;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(CREATED, READY, PLAYING, WAITING, WAITING_WITH_GAME);
    }

    @Override
    @CheckPlayerInEvent
    public void handle(final Message message, final String... args) {
    }

    private BiConsumer<Message, TgEvent> getCreatedReadyConsumer() {
        return (message, event) -> {
            playerService.leaveLobby(event, message.getFrom().getId());
            eventStateService.prepare(event);
//            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWaitingWIthGameConsumer() {
        return (message, event) -> {
            playerService.leaveLobby(event, message.getFrom().getId());
            eventStateService.active(event);
//            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }
}
