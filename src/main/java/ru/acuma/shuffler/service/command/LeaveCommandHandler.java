package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.aspect.marker.CheckPlayerInEvent;
import ru.acuma.shuffler.controller.LeaveCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.user.PlayerService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.CREATED;
import static ru.acuma.shuffler.model.constant.EventStatus.PLAYING;
import static ru.acuma.shuffler.model.constant.EventStatus.READY;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class LeaveCommandHandler extends BaseCommandHandler<LeaveCommand> {

    private final EventStateService eventStateService;
    private final PlayerService playerService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(CREATED, READY, PLAYING, WAITING, WAITING_WITH_GAME);
    }


    @Override
    @CheckPlayerInEvent
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
    }

    private BiConsumer<Message, TEvent> getCreatedReadyConsumer() {
        return (message, event) -> {
            playerService.leaveLobby(event, message.getFrom().getId());
            eventStateService.prepare(event);
//            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }

    private BiConsumer<Message, TEvent> getPlayingWaitingWaitingWIthGameConsumer() {
        return (message, event) -> {
            playerService.leaveLobby(event, message.getFrom().getId());
            eventStateService.active(event);
//            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }
}
