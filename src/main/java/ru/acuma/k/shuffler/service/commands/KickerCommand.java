package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;

import static ru.acuma.k.shuffler.model.enums.messages.MessageType.LOBBY;

@Component
public class KickerCommand extends BaseBotCommand {

    private final EventContextService eventContextService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public KickerCommand(EventContextService eventContextService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService, EventStateService eventStateService) {
        super(Command.KICKER.getCommand(), "Время покрутить шашлыки");
        this.eventContextService = eventContextService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        maintenanceService.sweepFromArgs(absSender, arguments, chat.getId());
        if (eventContextService.isActive(chat.getId())) {
            return;
        }
        var event = eventContextService.buildEvent(chat.getId());
        executeService.execute(absSender, messageService.sendMessage(event, LOBBY));
    }
}
