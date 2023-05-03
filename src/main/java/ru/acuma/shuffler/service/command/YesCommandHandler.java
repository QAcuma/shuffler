package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.YesCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TRender;
import ru.acuma.shuffler.service.event.ChampionshipService;
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
            case BEGIN_CHECKING -> beginGame(event);
            case GAME_CHECKING -> nextGame(event);
        }

    }

    private void beginGame(final TEvent event) {
        gameService.beginGame(event);
        eventStatusService.resume(event);
        event.render(TRender.forDelete(event.getMessageId(MessageType.CHECKING)));
        event.render(TRender.forUpdate(MessageType.LOBBY, event.getMessageId(MessageType.LOBBY)));
        event.render(TRender.forSend(MessageType.GAME));
    }

    private void nextGame(TEvent event) {
        gameService.finishGame(event);
        eventStatusService.resume(event);
        beginGame(event);

        event.render(TRender.forDelete(event.getMessageId(MessageType.GAME)));
    }

//
//    private BiConsumer<Message, TEvent> getCancelCheckingConsumer() {
//        return (message, event) -> championshipService.finishEvent(event);
//    }
//
//    private BiConsumer<Message, TEvent> getBeginCheckingConsumer() {
//        return (message, event) -> {
//            gameFacade.nextGameActions(event, message);
////            executeService.execute(messageService.buildLobbyMessageUpdate(event));
//        };
//    }
//
//    private BiConsumer<Message, TEvent> getCheckingConsumer() {
//        return (message, event) -> {
//            gameService.handleGameCheck(event);
//            gameFacade.finishGameActions(event, message);
//            gameFacade.nextGameActions(event, message);
//        };
//    }
//
//    private BiConsumer<Message, TEvent> getWaitingWithGameConsumer() {
//        return (message, event) -> {
//            gameService.handleGameCheck(event);
//            gameFacade.finishGameActions(event, message);
//        };
//    }
//
//    private BiConsumer<Message, TEvent> getFinishCheckingConsumer() {
//        return (message, event) -> eventFacade.finishEventActions(event, message);
//    }

}
