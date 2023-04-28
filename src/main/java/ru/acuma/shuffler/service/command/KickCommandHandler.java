package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.aspect.marker.CheckPlayerInEvent;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.controller.KickCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.KickService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.PLAYING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class KickCommandHandler extends BaseCommandHandler<KickCommand> {

    private final EventStateService eventStateService;
    private final KickService kickService;
    private final GameStateService gameStateService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(PLAYING, WAITING, WAITING_WITH_GAME);
    }


    @Override
    @SweepMessage
    @CheckPlayerInEvent
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
    }

    private BiConsumer<Message, TEvent> getPlayingWaitingWaitingWIthGameConsumer() {
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
