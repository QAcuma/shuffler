package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.EventStateService;
import ru.acuma.shuffler.service.ExecuteService;
import ru.acuma.shuffler.service.GameService;
import ru.acuma.shuffler.service.MaintenanceService;
import ru.acuma.shuffler.service.MessageService;
import ru.acuma.shuffler.model.enums.messages.MessageType;

@Component
public class RedCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final GameService gameService;

    public RedCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService, GameService gameService) {
        super(Command.RED.getCommand(), "Красные");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
        this.gameService = gameService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());

        eventStateService.redCheckingState(event);
        executeService.execute(absSender, messageService.updateMessage(event, event.getCurrentGame().getMessageId(), MessageType.GAME));
        executeService.execute(absSender, messageService.sendMessage(event, MessageType.CHECKING));
    }
}

