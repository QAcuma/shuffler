package ru.acuma.shuffler.service.command;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;

@Component
public class BeginCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final ExecuteService executeService;
    private final MessageService messageService;

    public BeginCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, ExecuteService executeService, MessageService messageService) {
        super(Command.BEGIN.getCommand(), "Начать турнир");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.executeService = executeService;
        this.messageService = messageService;
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());

        eventStateService.beginCheckState(event);
        executeService.execute(messageService.updateLobbyMessage(event));
        executeService.execute(messageService.sendMessage(event, MessageType.CHECKING));
    }
}

