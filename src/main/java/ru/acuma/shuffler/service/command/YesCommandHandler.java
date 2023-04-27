package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.controller.YesCommand;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.game.ChampionshipService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventState.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.constant.EventState.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.constant.EventState.FINISH_CHECKING;
import static ru.acuma.shuffler.model.constant.EventState.GAME_CHECKING;
import static ru.acuma.shuffler.model.constant.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class YesCommandHandler extends BaseCommandHandler<YesCommand> {

    private final ChampionshipService championshipService;
    private final GameService gameService;
    private final GameFacade gameFacade;
    private final EventFacade eventFacade;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(CANCEL_CHECKING, BEGIN_CHECKING, GAME_CHECKING, WAITING_WITH_GAME, FINISH_CHECKING);
    }

    @Override
    @SweepMessage
    public void handle(final Message message, final String... args) {

    }

    private BiConsumer<Message, TgEvent> getCancelCheckingConsumer() {
        return (message, event) -> championshipService.finishEvent(event);
    }

    private BiConsumer<Message, TgEvent> getBeginCheckingConsumer() {
        return (message, event) -> {
            gameFacade.nextGameActions(event, message);
//            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }

    private BiConsumer<Message, TgEvent> getCheckingConsumer() {
        return (message, event) -> {
            gameService.handleGameCheck(event);
            gameFacade.finishGameActions(event, message);
            gameFacade.nextGameActions(event, message);
        };
    }

    private BiConsumer<Message, TgEvent> getWaitingWithGameConsumer() {
        return (message, event) -> {
            gameService.handleGameCheck(event);
            gameFacade.finishGameActions(event, message);
        };
    }

    private BiConsumer<Message, TgEvent> getFinishCheckingConsumer() {
        return (message, event) -> eventFacade.finishEventActions(event, message);
    }

}
