package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.BeginCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.api.EventStateService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.READY;

@Service
@RequiredArgsConstructor
public class BeginCommandHandler extends BaseCommandHandler<BeginCommand> {

    private final EventStateService eventStateService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(READY);
    }


    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {

    }

    private BiConsumer<Message, TEvent> getReadyConsumer() {
        return (message, event) -> {
            eventStateService.begin(event);
//            var lobbyMessage = messageService.buildLobbyMessageUpdate(event);
//            var checkingMessage = messageService.buildMessage(event, MessageType.CHECKING);
//            executeService.execute(lobbyMessage);
//            executeService.execute(checkingMessage);
        };
    }
}
