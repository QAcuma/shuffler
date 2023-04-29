package ru.acuma.shuffler.service.telegram.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.constant.ExceptionCause;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageFilter implements UpdateFilter {

    @Override
    public void accept(final CallbackQuery callbackQuery) {
        var message = callbackQuery.getMessage();

        accept(message);
    }

    @Override
    public void accept(final Message message) {
        if (Objects.isNull(message) || Objects.isNull(message.getFrom())) {
            throw new DataException(ExceptionCause.MESSAGE_MISSING);
        }

        if (Objects.isNull(message.getFrom())) {
            throw new DataException(ExceptionCause.MESSAGE_FROM_MISSING);
        }
    }
}
