package ru.acuma.shuffler.service.command.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.controller.KickCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.command.common.BaseCommandHandler;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.event.GameStatusService;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.PLAYING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class KickCommandHandler extends BaseCommandHandler<KickCommand> {

    private final EventStatusService eventStatusService;
    private final GameStatusService gameStatusService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(PLAYING, WAITING_WITH_GAME);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        eventStatusService.gameChecking(event);
        gameStatusService.evictChecking(event.getCurrentGame());

        renderContext.forEvent(event).render(Render.forUpdate(MessageType.GAME))
            .render(Render.forMarkup(MessageType.LOBBY));
    }
}
