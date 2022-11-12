package ru.acuma.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.api.PlayerService;

@Component
public class LeaveCommand extends BaseBotCommand {

    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final PlayerService playerService;
    private final MessageService messageService;
    private final ExecuteService executeService;

    public LeaveCommand(EventContextServiceImpl eventContextService, EventStateService eventStateService, PlayerService playerService, MessageService messageService, ExecuteService executeService) {
        super(Command.LEAVE.getCommand(), "Покинуть список участников");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.playerService = playerService;
        this.messageService = messageService;
        this.executeService = executeService;
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());
        if (event == null || event.playerNotParticipate(message.getFrom().getId())) {
            return;
        }
        switch (event.getEventState()) {
            case CREATED:
            case READY:
                playerService.leaveLobby(event, message.getFrom().getId());
                eventStateService.definePreparingState(event);
                executeService.execute(messageService.updateLobbyMessage(event));
                break;
            case PLAYING:
            case WAITING:
            case WAITING_WITH_GAME:
                playerService.leaveLobby(event, message.getFrom().getId());
                eventStateService.defineActiveState(event);
                executeService.execute(messageService.updateLobbyMessage(event));
                break;
        }
    }

}

