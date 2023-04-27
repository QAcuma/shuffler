package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.FinishCommand;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventState.FINISH_CHECKING;
import static ru.acuma.shuffler.model.constant.EventState.PLAYING;
import static ru.acuma.shuffler.model.constant.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class FinishCommandHandler extends BaseCommandHandler<FinishCommand> {

    private final EventStateService eventStateService;
    private final GameStateService gameStateService;

    private final ExecuteService executeService;
    private final MessageService messageService;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(PLAYING, WAITING_WITH_GAME, FINISH_CHECKING);
    }

    @Override
    public void handle(Message message, String... args) {

    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWithGameConsumer() {
        return (message, event) -> {
            eventStateService.finishCheck(event);
            gameStateService.cancelCheck(event.getLatestGame());

            var lobbyMessage = messageService.buildReplyMarkupUpdate(event, event.getBaseMessage(), MessageType.LOBBY);
            var gameMessage = messageService.buildReplyMarkupUpdate(event, event.getLatestGame().getMessageId(), MessageType.GAME);
            var checkingMessage = messageService.buildMessage(event, MessageType.CHECKING_TIMED);

            executeService.execute(lobbyMessage);
            executeService.execute(gameMessage);
            executeService.executeRepeat(checkingMessage, event);
        };
    }

}
