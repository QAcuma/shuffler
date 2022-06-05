package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.EventStateService;
import ru.acuma.shuffler.service.ExecuteService;
import ru.acuma.shuffler.service.MessageService;

@Component
public class FinishCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final ExecuteService executeService;
    private final MessageService messageService;

    public FinishCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, ExecuteService executeService, MessageService messageService) {
        super(Command.FINISH.getCommand(), "Завершить чемпионат");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.executeService = executeService;
        this.messageService = messageService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());

        if (EventState.FINISH_CHECKING == event.getEventState() || !event.getPlayers().containsKey(message.getFrom().getId())) {
            return;
        }

        eventStateService.finishCheckState(event);
        executeService.execute(absSender, messageService.updateMessage(event, event.getCurrentGame().getMessageId(), MessageType.GAME));
        executeService.executeAsyncTimer(absSender, event, messageService.sendMessage(event, MessageType.CHECKING_TIMED));
    }
}

