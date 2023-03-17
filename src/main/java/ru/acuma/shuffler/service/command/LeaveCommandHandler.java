package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.LeaveCommand;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.aspect.CheckPlayerInEvent;
import ru.acuma.shuffler.service.user.PlayerService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.CREATED;
import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.READY;
import static ru.acuma.shuffler.model.enums.EventState.WAITING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class LeaveCommandHandler extends BaseCommandHandler<LeaveCommand> {

    private final ExecuteService executeService;
    private final MessageService messageService;
    private final EventStateService eventStateService;
    private final PlayerService playerService;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(CREATED, READY, PLAYING, WAITING, WAITING_WITH_GAME);
    }

    @Override
    @CheckPlayerInEvent
    public void handle(Message message) {
    }

    private BiConsumer<Message, TgEvent> getCreatedReadyConsumer() {
        return (message, event) -> {
            playerService.leaveLobby(event, message.getFrom().getId());
            eventStateService.prepare(event);
            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWaitingWIthGameConsumer() {
        return (message, event) -> {
            playerService.leaveLobby(event, message.getFrom().getId());
            eventStateService.active(event);
            executeService.execute(messageService.buildLobbyMessageUpdate(event));
        };
    }
}
