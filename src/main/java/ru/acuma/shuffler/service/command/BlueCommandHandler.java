package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.BlueCommand;
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
public class BlueCommandHandler extends CommandHandler<BlueCommand> {

    private final GameStateService gameStateService;
    private final EventStateService eventStateService;
    private final EventFacade eventFacade;
    private final CommandExecutorSourceFactory commandExecutorFactory;

    @Override
    protected void init() {
        commandExecutorFactory.register(PLAYING, getCommandClass(), getPlayingWaitingWithGameConsumer());
        commandExecutorFactory.register(WAITING_WITH_GAME, getCommandClass(), getPlayingWaitingWithGameConsumer());
    }

    @Override
    public Class<BlueCommand> getCommandClass() {
        return BlueCommand.class;
    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWithGameConsumer() {
        return (message, event) -> {
            eventStateService.gameCheck(event);
            gameStateService.blueCheck(event.getLatestGame());
            eventFacade.checkingStateActions(event);
        };
    }
}
