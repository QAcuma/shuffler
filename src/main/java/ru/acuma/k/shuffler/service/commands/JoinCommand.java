package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContext;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.util.BuildMessageUtil;

@Component
public class JoinCommand extends BaseBotCommand {

    private final EventContextService eventContextService;

    private final KeyboardService keyboardService;

    public JoinCommand(EventContext eventContext, EventContextService eventContextService, KeyboardService keyboardService) {
        super(Command.JOIN.getCommand(), "Присоединиться к игре");
        this.eventContextService = eventContextService;
        this.keyboardService = keyboardService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (eventContextService.isUserRegistered(chat.getId(), user)) {
            return;
        }
        var event = eventContextService.getEvent(chat.getId());

        event.getMembers().add(user);
        if (event.getMembers().size() >= eventContextService.getReadySize()) {
            event.setEventState(EventState.READY);
        }
        var keyboard = keyboardService.getKeyboard(event.getEventState());

        EditMessageText update = EditMessageText.builder()
                .chatId(String.valueOf(chat.getId()))
                .messageId(event.getBaseMessage())
                .text(BuildMessageUtil.buildCreatedMessage(eventContextService.getEvent(chat.getId())))
                .replyMarkup(keyboard)
                .build();

        absSender.execute(update);
    }
}

