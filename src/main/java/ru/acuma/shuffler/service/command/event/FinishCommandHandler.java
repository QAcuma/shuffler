package ru.acuma.shuffler.service.command.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.controller.FinishCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.command.common.BaseCommandHandler;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.event.GameStatusService;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.PLAYING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class FinishCommandHandler extends BaseCommandHandler<FinishCommand> {

    private final EventStatusService eventStatusService;
    private final GameStatusService gameStatusService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(PLAYING, WAITING, WAITING_WITH_GAME);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        switch (event.getEventStatus()) {
            case PLAYING, WAITING_WITH_GAME -> finishWithActiveGame(event);
            case WAITING -> finishWithoutGame(event);
        }
    }

    private void finishWithActiveGame(final TEvent event) {
        eventStatusService.finishCheck(event);
        gameStatusService.eventCheck(event.getCurrentGame());
        renderContext.forEvent(event).render(Render.forMarkup(MessageType.GAME))
            .render(Render.forUpdate(MessageType.LOBBY).withTimer());
    }

    private void finishWithoutGame(final TEvent event) {
        eventStatusService.finishCheck(event);
        renderContext.forEvent(event).render(Render.forUpdate(MessageType.LOBBY).withTimer());
    }
}
