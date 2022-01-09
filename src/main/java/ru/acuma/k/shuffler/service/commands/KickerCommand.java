package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;

import static ru.acuma.k.shuffler.model.enums.messages.MessageType.LOBBY;

@Component
public class KickerCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public KickerCommand(EventContextServiceImpl eventContextService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService, EventStateService eventStateService) {
        super(Command.KICKER.getCommand(), "Время покрутить шашлыки");
        this.eventContextService = eventContextService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        maintenanceService.sweepMessage(absSender, message);
        if (eventContextService.isActive(message.getChatId())) {
            return;
        }
        final var event = eventContextService.buildEvent(message.getChatId());
        var baseMessage = executeService.execute(absSender, messageService.sendMessage(event, LOBBY));
        executeService.execute(absSender, messageService.pinedMessage(baseMessage));
        event.watchMessage(baseMessage.getMessageId());
    }
}
