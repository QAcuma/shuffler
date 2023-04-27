package ru.acuma.shuffler.service.telegram.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.acuma.shuffler.exception.IdleException;
import ru.acuma.shuffler.model.constant.ExceptionCause;

@Service
@RequiredArgsConstructor
public class ChatFilter implements CallbackFilter {

    public static final String PRIVATE_CHAT = "private";

    @Override
    public void accept(CallbackQuery callbackQuery) {
        var chat = callbackQuery.getMessage().getChat();

        if (PRIVATE_CHAT.equals(chat.getType())) {
            throw new IdleException(ExceptionCause.CHAT_IS_PRIVATE);
        }
    }
}
