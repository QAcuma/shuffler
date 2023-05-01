package ru.acuma.shuffler.service.telegram.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.exception.IdleException;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.service.telegram.UserService;

@Service
@RequiredArgsConstructor
public class UserFilter implements AuthFilter {

    private final UserService userService;

    @Override
    public void accept(final CallbackQuery callbackQuery) {
        var message = callbackQuery.getMessage();
        filter(message);
    }

    @Override
    public void accept(final Message message) {
        filter(message);
    }

    private void filter(Message message) {
        var authStatus = userService.authenticate(message.getFrom().getId());

        switch (authStatus) {
            case UNREGISTERED -> userService.signUp(message.getFrom());
            case SUCCESS -> userService.update(message.getFrom());
            case DENY -> throw new IdleException(ExceptionCause.USER_IS_NOT_ACTIVE);
        }
    }
}
