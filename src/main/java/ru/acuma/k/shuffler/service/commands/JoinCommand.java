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
import ru.acuma.k.shuffler.service.MessageService;
import ru.acuma.k.shuffler.service.PlayerService;

import static ru.acuma.k.shuffler.model.enums.messages.MessageType.LOBBY;

@Component
public class JoinCommand extends BaseBotCommand {

    private final EventContextService eventContextService;
    private final EventStateService eventStateService;
    private final MessageService messageService;
    private final ExecuteService executeService;
    private final PlayerService playerService;

    public JoinCommand(EventContextService eventContextService, EventStateService eventStateService, MessageService messageService, ExecuteService executeService, PlayerService playerService) {
        super(Command.JOIN.getCommand(), "Присоединиться к игре");
        this.eventContextService = eventContextService;
        this.eventStateService = eventStateService;
        this.messageService = messageService;
        this.executeService = executeService;
        this.playerService = playerService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (eventContextService.isUserRegistered(chat.getId(), user)) {
            return;
        }
        var event = eventContextService.getEvent(chat.getId());

        eventContextService.registerPlayer(event.getChatId(), user);
        eventStateService.lobbyState(event);
        executeService.execute(absSender, messageService.updateMessage(event, event.getBaseMessage(), LOBBY));
    }
}

