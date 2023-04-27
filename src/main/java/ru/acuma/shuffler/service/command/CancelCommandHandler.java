package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.CancelCommand;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventState.CREATED;
import static ru.acuma.shuffler.model.constant.EventState.READY;

@Service
@RequiredArgsConstructor
public class CancelCommandHandler extends BaseCommandHandler<CancelCommand> {

    private final EventStateService eventStateService;

    private final ExecuteService executeService;
    private final MessageService messageService;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(CREATED, READY);
    }

    @Override
    public void handle(Message message, String... args) {

    }

    private BiConsumer<Message, TgEvent> getAnyConsumer() {
        return (message, event) -> {
            eventStateService.cancel(event);
            var lobbyMessage = messageService.buildLobbyMessageUpdate(event);
            executeService.execute(lobbyMessage);
            var checkingMessage = messageService.buildMessage(event, MessageType.CHECKING_TIMED);
            executeService.executeRepeat(checkingMessage, event);
        };
    }

}
