package ru.acuma.shuffler.service.telegram.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.service.telegram.GroupService;

@Service
@RequiredArgsConstructor
public class GroupFilter implements AuthFilter {

    private final GroupService groupService;

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
        boolean signedIn = groupService.signIn(message.getChat());

        if (!signedIn) {
            throw new DataException(ExceptionCause.GROUP_IS_NOT_ACTIVE, message.getChat().getId());
        }
    }
}
