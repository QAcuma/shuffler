package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.YesCommand;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TRender;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.event.GameService;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.FINISH_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.GAME_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class YesCommandHandler extends BaseCommandHandler<YesCommand> {

    private final GameService gameService;
    private final EventStatusService eventStatusService;

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
        event.render(TRender.forDelete(event.getMessageId(MessageType.LOBBY)))
            .render(TRender.forDelete(event.getMessageId(MessageType.CHECKING)))
            .render(TRender.forSend(MessageType.CANCELLED).withAfterAction(
                () -> TRender.forDelete(event.getMessageId(MessageType.CANCELLED)).withDelay(Constants.CANCELLED_MESSAGE_TTL_BEFORE_DELETE)
            ));
        eventContext.flushEvent(event.getChatId());
    }

    private void beginGame(final TEvent event) {
        gameService.beginGame(event);
        eventStatusService.resume(event);
        event.render(TRender.forDelete(event.getMessageId(MessageType.CHECKING)))
            .render(TRender.forUpdate(MessageType.LOBBY, event.getMessageId(MessageType.LOBBY)))
            .render(TRender.forSend(MessageType.GAME));
    }

    private void nextGame(TEvent event) {
        gameService.finishGame(event);
        eventStatusService.resume(event);
        beginGame(event);

        event.render(TRender.forDelete(event.getMessageId(MessageType.GAME)));
    }

    private void finishGame(TEvent event) {
        gameService.finishGame(event);
        eventStatusService.resume(event);

        event.render(TRender.forDelete(event.getMessageId(MessageType.GAME)))
            .render(TRender.forUpdate(MessageType.LOBBY, event.getMessageId(MessageType.LOBBY)));
    }

    private void finishEvent(TEvent event) {
        gameService.finishGame(event);
        eventStatusService.finished(event);
        eventContext.flushEvent(event.getChatId());

        event.render(TRender.forDelete(event.getMessageId(MessageType.GAME)))
            .render(TRender.forDelete(event.getMessageId(MessageType.CHECKING)))
            .render(TRender.forUpdate(MessageType.LOBBY, event.getMessageId(MessageType.LOBBY)));
    }
}
