package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.MaintenanceService;

@Slf4j
@Component
public class ResetCommand extends BaseBotCommand {

    private static final Long ID = 285250417L;

    private final EventContextService eventContextService;
    private final MaintenanceService maintenanceService;

    public ResetCommand(EventContextService eventContextService, MaintenanceService maintenanceService) {
        super(Command.RESET.getCommand(), "Сбросить бота");
        this.eventContextService = eventContextService;
        this.maintenanceService = maintenanceService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        maintenanceService.sweepFromArgs(absSender, arguments, chat.getId());
        if (user.getId().equals(ID)) {
            log.info("User {} initialize reset process in chat {}", user.getFirstName(), chat.getTitle());
            var event = eventContextService.getEvent(chat.getId());
            try {
                maintenanceService.sweepChat(absSender, event.getMessages(), chat.getId());
                maintenanceService.sweepEvent(event, false);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}

