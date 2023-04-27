package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.aspect.marker.CheckPlayerInEvent;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.controller.KickCommand;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.KickService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventState.PLAYING;
import static ru.acuma.shuffler.model.constant.EventState.WAITING;
import static ru.acuma.shuffler.model.constant.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class KickCommandHandler extends BaseCommandHandler<KickCommand> {

    private final EventStateService eventStateService;
    private final KickService kickService;
    private final GameStateService gameStateService;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(PLAYING, WAITING, WAITING_WITH_GAME);
    }

    @Override
    @SweepMessage
    @CheckPlayerInEvent
    public void handle(final Message message, final String... args) {
    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWaitingWIthGameConsumer() {
        return (message, event) -> {
            eventStateService.evicting(event);
            gameStateService.cancelCheck(event.getLatestGame());
            var method = kickService.prepareKickMessage(event);

//            executeService.execute(method);
//            executeService.execute(messageService.buildLobbyMessageUpdate(event));
//            executeService.execute(messageService.buildMessageUpdate(event, event.getLatestGame().getMessageId(), MessageType.GAME));
        };
    }
}
