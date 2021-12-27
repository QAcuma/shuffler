package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.service.DelayedService;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.util.BuildMessageUtil;

@Component
public class CancelCommand extends BaseBotCommand {

    private final EventContextService eventContextService;
    private final KeyboardService keyboardService;
    private final DelayedService delayedService;

    public CancelCommand(EventContextService eventContextService, KeyboardService keyboardService, DelayedService delayedService) {
        super(Command.CANCEL.getCommand(), "Отменить турнир");
        this.eventContextService = eventContextService;
        this.keyboardService = keyboardService;
        this.delayedService = delayedService;
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        var event = eventContextService.buildEvent(chat.getId());
        event.setEventState(EventState.CANCEL_CHECKING);

        EditMessageText edited = EditMessageText.builder()
                .chatId(String.valueOf(chat.getId()))
                .messageId(event.getBaseMessage())
                .text(BuildMessageUtil.buildCreatedMessage(event))
                .build();

        SendMessage response = SendMessage.builder()
                .text(BuildMessageUtil.buildCancelMessage(event))
                .chatId(String.valueOf(chat.getId()))
                .replyMarkup(keyboardService.getTimedCheckingKeyboard(delayedService.getTimeout()))
                .build();

        absSender.execute(edited);
        delayedService.processCheckingTimer(absSender, response, event);
    }
}

