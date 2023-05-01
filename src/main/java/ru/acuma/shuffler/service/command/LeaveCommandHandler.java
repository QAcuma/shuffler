package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.LeaveCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TRender;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.telegram.PlayerService;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.CREATED;
import static ru.acuma.shuffler.model.constant.EventStatus.PLAYING;
import static ru.acuma.shuffler.model.constant.EventStatus.READY;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class LeaveCommandHandler extends BaseCommandHandler<LeaveCommand> {

    private final EventStatusService eventStateService;
    private final PlayerService playerService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(CREATED, READY, PLAYING, WAITING, WAITING_WITH_GAME);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        switch (event.getEventStatus()) {
            case CREATED, READY -> leaveLobby(user, event);
            case PLAYING, WAITING, WAITING_WITH_GAME -> leaveEvent(user, event);
        }
        event.render(TRender.forUpdate(MessageType.LOBBY, event.getMessageId(MessageType.LOBBY)));
    }

    private void leaveLobby(final User user, final TEvent event) {
        playerService.leaveLobby(user, event);
        eventStateService.prepare(event);
    }

    private void leaveEvent(final User user, final TEvent event) {
        playerService.leaveEvent(user, event);
        eventStateService.resume(event);
    }
}
