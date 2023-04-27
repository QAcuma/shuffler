package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.aspect.marker.CheckNoActiveEvent;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.controller.EventCommand;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shuffler.model.constant.messages.MessageAfterAction;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.service.message.Render;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventCommandHandler extends BaseCommandHandler<EventCommand> {

    private final EventContext eventContext;

    @Override
    @SweepMessage
    @CheckNoActiveEvent
    public void handle(final Message message, final String... args) {
        beginEvent(message.getChatId(), Discipline.KICKER);
    }

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(EventState.ANY);
    }

    private void beginEvent(final Long chatId, final Discipline discipline) {
        eventContext.createEvent(chatId, discipline)
            .action(
                MessageType.LOBBY,
                Render.forSend().withAfterAction(MessageAfterAction.PIN)
            );
    }
}
