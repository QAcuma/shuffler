package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.JoinCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.user.PlayerService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.CREATED;
import static ru.acuma.shuffler.model.constant.EventStatus.PLAYING;
import static ru.acuma.shuffler.model.constant.EventStatus.READY;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class JoinCommandHandler extends BaseCommandHandler<JoinCommand> {

    private final EventStateService eventStateService;
    private final GameFacade gameFacade;
    private final PlayerService playerService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(CREATED, READY, PLAYING, WAITING, WAITING_WITH_GAME);
    }

    @Override
    protected void invokeEventCommand(final User user, final TEvent event, final String[] args) {
        switch (event.getEventStatus()) {
            case CREATED, READY -> joinPlayer(user, event);
            case PLAYING -> {}
            case WAITING -> {}
            case WAITING_WITH_GAME -> {}
        }
    }

    private void joinPlayer(final User user, final TEvent event) {
        playerService.getEventPlayer(user, event);
        eventStateService.prepare(event);
//      executeService.execute(messageService.buildMessageUpdate(event, event.getLobbyMessageId(), MessageType.LOBBY));
    }

    private BiConsumer<Message, TEvent> getPlayingConsumer(final User user, final TEvent event) {
        playerService.joinLobby(event, message.getFrom());
//            executeService.execute(messageService.buildLobbyMessageUpdate(event));
    }

    private BiConsumer<Message, TEvent> getWaitingConsumer() {
        return (message, event) -> {
            playerService.joinLobby(event, message.getFrom());
            eventStateService.active(event);
//            executeService.execute(messageService.buildLobbyMessageUpdate(event));
            gameFacade.nextGameActions(event, message);
        };

    }

    private BiConsumer<Message, TEvent> getWaitingWithGameConsumer() {
        return (message, event) -> {
            playerService.joinLobby(event, message.getFrom());
            eventStateService.active(event);

//            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }
}
