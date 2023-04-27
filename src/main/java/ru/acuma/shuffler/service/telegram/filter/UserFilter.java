package ru.acuma.shuffler.service.telegram.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.service.user.UserService;

@Service
@RequiredArgsConstructor
public class UserFilter implements AuthFilter {

    private final UserService userService;

    @Override
    public void accept(CallbackQuery callbackQuery) {
        var message = callbackQuery.getMessage();
        var signedIn = userService.signIn(message.getFrom());
        if (!signedIn) {
            throw new DataException(ExceptionCause.USER_IS_NOT_ACTIVE, message.getFrom().getId());
        }
    }
}
