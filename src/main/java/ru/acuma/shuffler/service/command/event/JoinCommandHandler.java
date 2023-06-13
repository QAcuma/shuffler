package ru.acuma.shuffler.service.command.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.controller.JoinCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.command.common.BaseCommandHandler;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.event.GameService;
import ru.acuma.shuffler.service.telegram.PlayerService;

import java.util.List;
import java.util.Optional;

import static ru.acuma.shuffler.model.constant.EventStatus.CREATED;
import static ru.acuma.shuffler.model.constant.EventStatus.PLAYING;
import static ru.acuma.shuffler.model.constant.EventStatus.READY;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class JoinCommandHandler extends BaseCommandHandler<JoinCommand> {

    private final EventStatusService eventStatusService;
    private final PlayerService playerService;
    private final GameService gameService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(CREATED, READY, PLAYING, WAITING, WAITING_WITH_GAME);
    }

    @Override
    protected void invokeEventCommand(final User user, final TEvent event, final String... args) {
        playerService.join(user, event);

        switch (event.getEventStatus()) {
            case CREATED, READY -> joinLobby(event);
            case PLAYING, WAITING_WITH_GAME -> joinEvent(event);
            case WAITING -> joinWaitingEvent(event);
            default -> idle(event.getEventStatus());
        }
    }

    private void joinLobby(final TEvent event) {
        eventStatusService.praperation(event);
        renderContext.forEvent(event).render(Render.forUpdate(MessageType.LOBBY));
    }

    private void joinEvent(final TEvent event) {
        eventStatusService.resume(event);
        renderContext.forEvent(event).render(Render.forUpdate(MessageType.LOBBY));
    }

    private void joinWaitingEvent(final TEvent event) {
        Optional.of(eventStatusService.resume(event))
            .filter(PLAYING::equals)
            .ifPresentOrElse(
                status -> {
                    gameService.beginGame(event);
                    renderContext.forEvent(event).render(Render.forSend(MessageType.GAME))
                        .render(Render.forUpdate(MessageType.LOBBY));
                },
                () -> renderContext.forEvent(event).render(Render.forUpdate(MessageType.LOBBY))
            );
    }
}
