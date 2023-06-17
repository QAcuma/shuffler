package ru.acuma.shuffler.service.telegram.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.exception.IdleException;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.service.telegram.ChatService;

import static ru.acuma.shuffler.config.CacheConfig.GROUP_AUTH;

@Service
@RequiredArgsConstructor
public class GroupFilter implements AuthFilter {

    private final ChatService chatService;

    @Override
    public void accept(final CallbackQuery callbackQuery) {
        var message = callbackQuery.getMessage();
        filter(message);
    }

    @Override
    public void accept(final Message message) {
        filter(message);
    }

    private void filter(final Message message) {
        var authStatus = chatService.authenticate(message.getChat().getId());

        switch (authStatus) {
            case UNREGISTERED -> chatService.signUp(message.getChat());
            case SUCCESS -> chatService.update(message.getChat());
            case DENY -> throw new IdleException(ExceptionCause.GROUP_IS_NOT_ACTIVE);
        }
    }
}
