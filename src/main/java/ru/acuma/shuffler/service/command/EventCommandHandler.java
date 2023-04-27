package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.aspect.marker.CheckNoActiveEvent;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.controller.EventCommand;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventCommandHandler extends BaseCommandHandler<EventCommand> {
    private final EventContext eventContext;

    @Override
    @SweepMessage
    @CheckNoActiveEvent
    public void handle(Message message, String... args) {
        var event = eventContext.findEvent(message.getChatId());
    }

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(EventState.ANY);
    }

    private void beginEvent(Long chatId, Discipline discipline) {
//        var event = eventContext.createEvent(chatId, discipline);
//        var response = messageService.buildMessage(event, MessageType.LOBBY);
//
//        var baseMessage = executeService.execute(response);
//        var pinned = messageService.pinMessage(baseMessage);
//        executeService.execute(pinned);

    }
}
