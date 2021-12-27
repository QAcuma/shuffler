package ru.acuma.k.shuffler.service.commands;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventHolder;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.util.BuildMessageUtil;

@Component
public class LeaveCommand extends CallbackBotCommand {

    @Autowired
    EventHolder eventHolder;

    @Autowired
    KeyboardService keyboardService;

    public LeaveCommand() {
        super(Command.LEAVE.getValue(), "Покинуть список участников");
    }

    @SneakyThrows
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (!eventHolder.isRegistered(chat.getId(), user)){
            return;
        }

        var keyboard = keyboardService.getKeyboard(chat.getId());

        eventHolder.removeEventMember(chat.getId(), user);

        EditMessageText edited = EditMessageText.builder()
                .chatId(String.valueOf(chat.getId()))
                .messageId(eventHolder.getEvent(chat.getId()).getMessageId())
                .text(BuildMessageUtil.buildCreatedMessage(eventHolder.getEvent(chat.getId())))
                .replyMarkup(keyboard)
                .build();
        absSender.execute(edited);
    }
}

