package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.YesCommand;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.service.api.ChampionshipService;
import ru.acuma.shuffler.service.api.EventContextService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.aspect.SweepMessage;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.FINISH_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.GAME_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class YesCommandHandler extends CommandHandler<YesCommand> {

    private final EventContextService eventContextService;
    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final ChampionshipService championshipService;
    private final MessageService messageService;
    private final GameService gameService;
    private final GameFacade gameFacade;
    private final EventFacadeImpl eventFacade;

    @Override
    public void init() {
        commandExecutorFactory.register(CANCEL_CHECKING, getCommandClass(), getCancelCheckingConsumer());
        commandExecutorFactory.register(BEGIN_CHECKING, getCommandClass(), getBeginCheckingConsumer());
        commandExecutorFactory.register(GAME_CHECKING, getCommandClass(), getCheckingConsumer());
        commandExecutorFactory.register(WAITING_WITH_GAME, getCommandClass(), getWaitingWithGameConsumer());
        commandExecutorFactory.register(FINISH_CHECKING, getCommandClass(), getFinishCheckingConsumer());
    }

    @Override
    public Class<YesCommand> getCommandClass() {
        return YesCommand.class;
    }

    @Override
    @SweepMessage
    public void handle(Message message) {
        super.handle(message);
    }


    private BiConsumer<Message, TgEvent> getCancelCheckingConsumer() {
        return (message, event) -> championshipService.finishEvent(event);
    }

    private BiConsumer<Message, TgEvent> getBeginCheckingConsumer() {
        return (message, event) -> {
            gameFacade.nextGameActions(event, message);
            executeService.execute(messageService.updateLobbyMessage(event));
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
