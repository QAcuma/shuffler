package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.JoinCommand;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.aspect.marker.CheckPlayerNotInEvent;
import ru.acuma.shuffler.service.user.PlayerService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventState.CREATED;
import static ru.acuma.shuffler.model.constant.EventState.PLAYING;
import static ru.acuma.shuffler.model.constant.EventState.READY;
import static ru.acuma.shuffler.model.constant.EventState.WAITING;
import static ru.acuma.shuffler.model.constant.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class JoinCommandHandler extends BaseCommandHandler<JoinCommand> {

    private final ExecuteService executeService;
    private final MessageService messageService;
    private final GameFacade gameFacade;
    private final EventStateService eventStateService;
    private final PlayerService playerService;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(CREATED, READY, PLAYING, WAITING, WAITING_WITH_GAME);
    }

    @Override
    @CheckPlayerNotInEvent
    public void handle(Message message, String... args) {
    }


    private void joinPlayer(Message message, TgEvent event) {
            playerService.getEventPlayer(message.getFrom(), event);
            eventStateService.prepare(event);
            executeService.execute(messageService.buildMessageUpdate(event, event.getBaseMessage(), MessageType.LOBBY));
    }

    private BiConsumer<Message, TgEvent> getPlayingConsumer() {
        return (message, event) -> {
            playerService.joinLobby(event, message.getFrom());
            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }

    private BiConsumer<Message, TgEvent> getWaitingConsumer() {
        return (message, event) -> {
            playerService.joinLobby(event, message.getFrom());
            eventStateService.active(event);
            executeService.execute(messageService.buildLobbyMessageUpdate(event));
            gameFacade.nextGameActions(event, message);
        };

    }

    private BiConsumer<Message, TgEvent> getWaitingWithGameConsumer() {
        return (message, event) -> {
            playerService.joinLobby(event, message.getFrom());
            eventStateService.active(event);
            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }
}
