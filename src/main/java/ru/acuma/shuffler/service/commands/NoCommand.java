package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

@Component
public class NoCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final MaintenanceService maintenanceService;
    private final EventStateService eventStateService;
    private final GameStateService gameStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public NoCommand(EventContextServiceImpl eventContextService, MaintenanceService maintenanceService, GameStateService gameStateService, EventStateService eventStateService, MessageService messageService, ExecuteService executeService) {
        super(Command.NO.getCommand(), "Нет");
        this.eventContextService = eventContextService;
        this.gameStateService = gameStateService;
        this.maintenanceService = maintenanceService;
        this.eventStateService = eventStateService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, Message message) {
        maintenanceService.sweepMessage(absSender, message);
        var event = eventContextService.getCurrentEvent(message.getChatId());

        if (event == null) {
            return;
        }

        switch (event.getEventState()) {
            case CANCEL_CHECKING:
            case BEGIN_CHECKING:
                eventStateService.definePreparingState(event);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                break;
            case PLAYING:
            case WAITING_WITH_GAME:
                gameStateService.activeState(event.getLatestGame());
                eventStateService.defineActiveState(event);
                executeService.execute(absSender, messageService.updateLobbyMessage(event));
                executeService.execute(
                        absSender,
                        messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME));
                break;
            case WAITING:
            case FINISH_CHECKING:
                eventStateService.defineActiveState(event);
                executeService.execute(
                        absSender,
                        messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME));
                break;
        }
    }
}

