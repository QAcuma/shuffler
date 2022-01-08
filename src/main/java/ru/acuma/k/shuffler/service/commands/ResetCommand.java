package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.MaintenanceService;

@Slf4j
@Component
public class ResetCommand extends BaseBotCommand {

    private static final Long ID = 285250417L;

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;

    public ResetCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, MaintenanceService maintenanceService) {
        super(Command.RESET.getCommand(), "Сбросить бота");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.maintenanceService = maintenanceService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        maintenanceService.sweepMessage(absSender, message);
        if (message.getFrom().getId().equals(ID)) {
            log.info("User {} initialized reset process in chat {}", message.getFrom().getFirstName(), message.getChat().getTitle());
            final var event = eventContextService.getEvent(message.getChatId());
            try {
                eventStateService.cancelledState(event);
                maintenanceService.sweepChat(absSender, event);
                maintenanceService.sweepEvent(event, false);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}

