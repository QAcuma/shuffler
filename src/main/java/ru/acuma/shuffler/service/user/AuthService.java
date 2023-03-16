package ru.acuma.shuffler.service.user;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.service.aspect.GroupAuth;
import ru.acuma.shuffler.service.aspect.UserAuth;

import java.util.Objects;

@Service
public class AuthService {

    public static final String PRIVATE_CHAT = "private";

    @UserAuth
    @GroupAuth
    public boolean doAuth(Message message, String... args) {
        var chat = message.getChat();

        return PRIVATE_CHAT.equals(chat.getType()) || Objects.isNull(message.getFrom());
    }
}
