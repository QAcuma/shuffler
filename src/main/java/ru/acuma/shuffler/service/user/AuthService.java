package ru.acuma.shuffler.service.user;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.service.aspect.GroupAuth;
import ru.acuma.shuffler.service.aspect.UserAuth;

import java.util.Objects;

@Service
public class AuthService {

    @UserAuth
    @GroupAuth
    public boolean doAuth(Message message) {
        return Objects.isNull(message.getFrom());
    }
}
