package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.GameService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;

import static ru.acuma.k.shuffler.model.enums.messages.MessageType.CHECKING;
import static ru.acuma.k.shuffler.model.enums.messages.MessageType.GAME;

@Component
public class BlueCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final GameService gameService;

    public BlueCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService, GameService gameService) {
        super(Command.BLUE.getCommand(), "Синие");
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
        final var event = eventContextService.buildEvent(message.getChatId());

        eventStateService.blueCheckingState(event);
        executeService.execute(absSender, messageService.updateMessage(event, event.getLastGame().getMessageId(), GAME));
        executeService.execute(absSender, messageService.sendMessage(event, CHECKING));
    }
}

