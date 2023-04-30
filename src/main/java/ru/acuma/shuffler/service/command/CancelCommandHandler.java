package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.CancelCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.message.MessageService;
import ru.acuma.shuffler.model.domain.Render;
import ru.acuma.shuffler.service.telegram.ExecuteService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.CREATED;
import static ru.acuma.shuffler.model.constant.EventStatus.READY;

@Service
@RequiredArgsConstructor
public class CancelCommandHandler extends BaseCommandHandler<CancelCommand> {

    private final EventStatusService eventStateService;

    private final ExecuteService executeService;
    private final MessageService messageService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(CREATED, READY);
    }


    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {

    }

    private BiConsumer<Message, TEvent> getAnyConsumer() {
        return (message, event) -> {
            eventStateService.cancel(event);
            var lobbyMessage = messageService.buildLobbyMessageUpdate(event);
//            executeService.execute(lobbyMessage);
            var checkingMessage = messageService.buildMessage(event, MessageType.CHECKING_TIMED);
//            event.render(MessageType.CHECKING, Render.forSend().withSchedule());
//            executeService.executeRepeat(checkingMessage, event);
        };
    }

}
