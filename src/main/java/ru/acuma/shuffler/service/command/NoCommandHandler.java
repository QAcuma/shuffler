package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.context.Render;
import ru.acuma.shuffler.controller.NoCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.event.GameStatusService;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.EVICTING;
import static ru.acuma.shuffler.model.constant.EventStatus.FINISH_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.GAME_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING;

@Service
@RequiredArgsConstructor
public class NoCommandHandler extends BaseCommandHandler<NoCommand> {

    private final EventStatusService eventStatusService;
    private final GameStatusService gameStatusService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(CANCEL_CHECKING, BEGIN_CHECKING, GAME_CHECKING, EVICTING, WAITING, FINISH_CHECKING);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        renderContext.forEvent(event).flushFutures();
        switch (event.getEventStatus()) {
            case CANCEL_CHECKING, BEGIN_CHECKING -> returnLobby(event);
            case WAITING -> returnWaitingLobby(event);
            case GAME_CHECKING, FINISH_CHECKING, EVICTING -> returnGame(event);
        }
    }

    private void returnLobby(final TEvent event) {
        eventStatusService.praperation(event);
        renderContext.forEvent(event).render(Render.forUpdate(MessageType.LOBBY));
    }

    private void returnWaitingLobby(final TEvent event) {
        eventStatusService.resume(event);
        renderContext.forEvent(event).render(Render.forUpdate(MessageType.LOBBY));
    }

    private void returnGame(final TEvent event) {
        eventStatusService.resume(event);
        gameStatusService.active(event.getCurrentGame());
        renderContext.forEvent(event).render(Render.forUpdate(MessageType.LOBBY))
            .render(Render.forUpdate(MessageType.GAME));
    }
}
