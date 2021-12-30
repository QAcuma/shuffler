package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.EventStateService;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.GameService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;

import static ru.acuma.k.shuffler.model.enums.Values.CANCELLED_MESSAGE_TIMEOUT;
import static ru.acuma.k.shuffler.model.enums.messages.MessageType.GAME;

@Component
public class YesCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final GameService gameService;

    public YesCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, MaintenanceService maintenanceService, MessageService messageService, ExecuteService executeService, GameService gameService) {
        super(Command.YES.getCommand(), "Да");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.executeService = executeService;
        this.gameService = gameService;
    }

    @Override
    public void execute(AbsSender absSender, Message message) {
        final var event = eventContextService.getEvent(message.getChatId());
        switch (event.getEventState()) {
            case BEGIN_CHECKING:
                beginChampionship(absSender, event, message);
                break;
            case CANCEL_CHECKING:
                cancelChampionship(absSender, event);
                break;
            default:
        }
    }

    @SneakyThrows
    private void beginChampionship(AbsSender absSender, KickerEvent event, Message message) {
        gameService.buildGame(event);
        eventStateService.playingState(event);
        executeService.execute(absSender, messageService.updateLobbyMessage(event));
        maintenanceService.sweepMessage(absSender, message);
        executeService.execute(absSender, messageService.sendMessage(event, GAME));

    }

    @SneakyThrows
    private void cancelChampionship(AbsSender absSender, KickerEvent event) {
        eventStateService.cancelledState(event);

        var update = messageService.updateLobbyMessage(event);
        executeService.execute(absSender, update);

        event.missMessage(event.getBaseMessage());
        maintenanceService.sweepChat(absSender, event);
        maintenanceService.sweepEvent(event, false);

        executeService.executeLater(absSender,
                () -> maintenanceService.sweepMessage(absSender, Long.valueOf(update.getChatId()), update.getMessageId()),
                CANCELLED_MESSAGE_TIMEOUT
        );
    }
}

