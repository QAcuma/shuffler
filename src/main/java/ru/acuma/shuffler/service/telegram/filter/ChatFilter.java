package ru.acuma.shuffler.service.telegram.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.exception.IdleException;
import ru.acuma.shuffler.model.constant.ExceptionCause;

@Service
@RequiredArgsConstructor
public class ChatFilter implements UpdateFilter {

    public static final String PRIVATE_CHAT = "private";

    @Override
    public void accept(final CallbackQuery callbackQuery) {
        var message = callbackQuery.getMessage();

        accept(message);
    }

    @Override
    public void accept(final Message message) {
        var chat = message.getChat();

        if (PRIVATE_CHAT.equals(chat.getType())) {
            throw new IdleException(ExceptionCause.CHAT_IS_PRIVATE);
        }
    }
}
