package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.controller.YesCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.game.ChampionshipService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.FINISH_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.GAME_CHECKING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class YesCommandHandler extends BaseCommandHandler<YesCommand> {

    private final ChampionshipService championshipService;
    private final GameService gameService;
    private final GameFacade gameFacade;
    private final EventFacade eventFacade;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(CANCEL_CHECKING, BEGIN_CHECKING, GAME_CHECKING, WAITING_WITH_GAME, FINISH_CHECKING);
    }


    @Override
    @SweepMessage
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {

    }

    private BiConsumer<Message, TEvent> getCancelCheckingConsumer() {
        return (message, event) -> championshipService.finishEvent(event);
    }

    private BiConsumer<Message, TEvent> getBeginCheckingConsumer() {
        return (message, event) -> {
            gameFacade.nextGameActions(event, message);
//            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }

    private BiConsumer<Message, TEvent> getCheckingConsumer() {
        return (message, event) -> {
            gameService.handleGameCheck(event);
            gameFacade.finishGameActions(event, message);
            gameFacade.nextGameActions(event, message);
        };
    }

    private BiConsumer<Message, TEvent> getWaitingWithGameConsumer() {
        return (message, event) -> {
            gameService.handleGameCheck(event);
            gameFacade.finishGameActions(event, message);
        };
    }

    private BiConsumer<Message, TEvent> getFinishCheckingConsumer() {
        return (message, event) -> eventFacade.finishEventActions(event, message);
    }

}
