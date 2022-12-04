package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.FinishCommand;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.FINISH_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class FinishCommandHandler extends CommandHandler<FinishCommand> {

    private final EventStateService eventStateService;
    private final GameStateService gameStateService;
    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final ExecuteService executeService;
    private final MessageService messageService;

    @Override
    public void init() {
        commandExecutorFactory.register(PLAYING, getCommandClass(), getPlayingWaitingWithGameConsumer());
        commandExecutorFactory.register(WAITING_WITH_GAME, getCommandClass(), getPlayingWaitingWithGameConsumer());
//        commandExecutorFactory.register(FINISH_CHECKING, getCommandClass(), getPlayingWaitingWithGameConsumer());
        commandExecutorFactory.register(FINISH_CHECKING, getCommandClass(), (message, event) -> {});
    }

    @Override
    public Class<FinishCommand> getCommandClass() {
        return FinishCommand.class;
    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWithGameConsumer() {
        return (message, event) -> {
            eventStateService.finishCheck(event);
            gameStateService.cancelCheck(event.getLatestGame());

            var lobbyMessage = messageService.updateMarkup(event, event.getBaseMessage(), MessageType.LOBBY);
            var gameMessage = messageService.updateMarkup(event, event.getLatestGame().getMessageId(), MessageType.GAME);
            var checkingMessage = messageService.sendMessage(event, MessageType.CHECKING_TIMED);

            executeService.execute(lobbyMessage);
            executeService.execute(gameMessage);
            executeService.executeRepeat(checkingMessage, event);
        };
    }

}
