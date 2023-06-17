package ru.acuma.shuffler.service.telegram.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.exception.IdleException;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.wrapper.SearchPlayerParams;
import ru.acuma.shuffler.service.telegram.PlayerService;
import ru.acuma.shuffler.service.telegram.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFilter implements AuthFilter {

    private final UserService userService;
    private final PlayerService playerService;

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
        var userStatus = userService.authenticate(message.getFrom().getId());
        switch (userStatus) {
            case UNREGISTERED -> userService.signUp(message.getFrom());
            case SUCCESS -> userService.update(message.getFrom());
            case DENY -> throw new IdleException(ExceptionCause.USER_IS_NOT_ACTIVE);
        }
        var searchPlayerParams = new SearchPlayerParams(message.getFrom().getId(), message.getChatId());
        var playerStatus = playerService.authenticate(searchPlayerParams);
        switch (playerStatus) {
            case UNREGISTERED -> playerService.signUp(searchPlayerParams.chatId(), searchPlayerParams.userId());
            case SUCCESS -> log.trace("Player {} authenticated for chat {}", searchPlayerParams.chatId(), searchPlayerParams.userId());
            case DENY -> throw new IdleException(ExceptionCause.PLAYER_NOT_FOUND);
        }
    }
}
