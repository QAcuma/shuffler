package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.controller.EventCommand;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.aspect.CheckNoActiveEvent;
import ru.acuma.shuffler.service.aspect.SweepMessage;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventCommandHandler extends BaseCommandHandler<EventCommand> {
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final EventContext eventContext;

    @Override
    @SweepMessage
    @CheckNoActiveEvent
    public void handle(Message message) {
        var event = eventContext.findEvent(message.getChatId());
    }

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(EventState.ANY);
    }

    private void beginEvent(Long chatId, Discipline discipline) {
        var event = eventContext.createEvent(chatId, discipline);
        var response = messageService.buildMessage(event, MessageType.LOBBY);

        var baseMessage = executeService.execute(response);
        var pinned = messageService.pinMessage(baseMessage);
        executeService.execute(pinned);
        event.spyMessage(baseMessage.getMessageId());

    }
}
