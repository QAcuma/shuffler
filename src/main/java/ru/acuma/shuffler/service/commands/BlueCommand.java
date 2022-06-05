package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.EventStateService;
import ru.acuma.shuffler.service.ExecuteService;
import ru.acuma.shuffler.service.MessageService;

@Component
public class BlueCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public BlueCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, MessageService messageService, ExecuteService executeService) {
        super(Command.BLUE.getCommand(), "Синие");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());

        eventStateService.blueCheckingState(event);
        executeService.execute(absSender, messageService.updateMessage(event, event.getCurrentGame().getMessageId(), MessageType.GAME));
        executeService.execute(absSender, messageService.sendMessage(event, MessageType.CHECKING));
    }
}

