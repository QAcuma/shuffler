package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shufflerlib.model.Discipline;

@Component
public class PingPongCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public PingPongCommand(EventContextServiceImpl eventContextService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService) {
        super(Command.PING_PONG.getCommand(), "Время пинать сферическую штуку");
        this.eventContextService = eventContextService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        maintenanceService.sweepMessage(message);
        if (eventContextService.isActive(message.getChatId())) {
            return;
        }
        final var event = eventContextService.buildEvent(message.getChatId(), Discipline.PING_PONG);
        var baseMessage = executeService.execute(messageService.sendMessage(event, MessageType.LOBBY));
        executeService.execute(messageService.pinedMessage(baseMessage));
        event.watchMessage(baseMessage.getMessageId());
    }
}
