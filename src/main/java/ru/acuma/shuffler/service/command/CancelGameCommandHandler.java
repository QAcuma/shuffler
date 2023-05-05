package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.CancelGameCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.Render;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.event.GameStatusService;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.PLAYING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class CancelGameCommandHandler extends BaseCommandHandler<CancelGameCommand> {

    private final GameStatusService gameStatusService;
    private final EventStatusService eventStatusService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(PLAYING, WAITING_WITH_GAME);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        eventStatusService.gameCheck(event);
        gameStatusService.cancelCheck(event.getCurrentGame());

        event.render(Render.forUpdate(MessageType.LOBBY))
            .render(Render.forUpdate(MessageType.GAME).withTimer());
    }
}
