package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.YesCommand;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.Render;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.event.GameService;
import ru.acuma.shuffler.service.event.GameStatusService;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.FINISH_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.GAME_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class YesCommandHandler extends BaseCommandHandler<YesCommand> {

    private final GameService gameService;
    private final EventStatusService eventStatusService;
    private final GameStatusService gameStatusService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(CANCEL_CHECKING, BEGIN_CHECKING, GAME_CHECKING, WAITING_WITH_GAME, FINISH_CHECKING);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        switch (event.getEventStatus()) {
            case CANCEL_CHECKING -> cancelEvent(event);
            case BEGIN_CHECKING -> beginGame(event);
            case GAME_CHECKING -> nextGame(event);
            case WAITING_WITH_GAME -> finishGame(event);
            case FINISH_CHECKING -> finishEvent(event);
        }
    }

    private void cancelEvent(final TEvent event) {
        eventStatusService.cancelled(event);
        event.render(Render.forDelete(event.getMessageId(MessageType.LOBBY)))
            .render(Render.forSend(MessageType.CANCELLED).withAfterAction(
                () -> Render.forDelete(event.getMessageId(MessageType.CANCELLED))
                    .withDelay(Constants.CANCELLED_MESSAGE_TTL_BEFORE_DELETE)
            ));
    }

    private void beginGame(final TEvent event) {
        gameService.beginGame(event);
        eventStatusService.resume(event);
        event.render(Render.forUpdate(MessageType.LOBBY))
            .render(Render.forSend(MessageType.GAME));
    }

    private void nextGame(final TEvent event) {
        gameService.finishGame(event);
        switch (eventStatusService.resume(event)) {
            case PLAYING -> {
                gameService.beginGame(event);
                event.render(Render.forUpdate(MessageType.LOBBY))
                    .render(Render.forUpdate(MessageType.GAME));
            }
            case WAITING -> event.render(Render.forUpdate(MessageType.LOBBY))
                .render(Render.forDelete(event.getMessageId(MessageType.GAME)));
        }
    }

    private void finishGame(final TEvent event) {
        gameStatusService.finished(event.getCurrentGame());
        gameService.finishGame(event);
        eventStatusService.resume(event);

        event.render(Render.forDelete(event.getMessageId(MessageType.GAME)))
            .render(Render.forUpdate(MessageType.LOBBY));
    }

    private void finishEvent(final TEvent event) {
        gameStatusService.finished(event.getCurrentGame());
        gameService.finishGame(event);
        eventStatusService.finished(event);

        event.render(Render.forDelete(event.getMessageId(MessageType.GAME)))
            .render(Render.forUpdate(MessageType.LOBBY));
    }
}
