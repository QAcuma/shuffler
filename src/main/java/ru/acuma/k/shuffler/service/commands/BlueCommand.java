package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.MessageService;
import ru.acuma.shufflerlib.model.Discipline;

import static ru.acuma.k.shuffler.model.enums.messages.MessageType.CHECKING;
import static ru.acuma.k.shuffler.model.enums.messages.MessageType.GAME;

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
        executeService.execute(absSender, messageService.updateMessage(event, event.getCurrentGame().getMessageId(), GAME));
        executeService.execute(absSender, messageService.sendMessage(event, CHECKING));
    }
}

