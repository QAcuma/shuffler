package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.CancelGameCommand;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.service.api.EventFacade;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class CancelGameCommandHandler extends CommandHandler<CancelGameCommand> {

    private final GameStateService gameStateService;
    private final EventStateService eventStateService;
    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final EventFacade eventFacade;

    @Override
    public void init() {
        commandExecutorFactory.register(PLAYING, getCommandClass(), getCancelConsumer());
        commandExecutorFactory.register(WAITING_WITH_GAME, getCommandClass(), getCancelConsumer());
    }

    @Override
    public Class<CancelGameCommand> getCommandClass() {
        return CancelGameCommand.class;
    }

    @Override
    public void handle(Message message) {
        super.handle(message);
    }

    private BiConsumer<Message, TgEvent> getCancelConsumer() {
        return (message, event) -> {
            eventStateService.gameCheck(event);
            gameStateService.cancelCheck(event.getLatestGame());
            eventFacade.checkingStateActions(event);
        };
    }

}
