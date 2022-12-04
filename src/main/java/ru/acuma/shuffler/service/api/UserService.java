package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.tables.pojos.UserInfo;

import java.util.List;

public interface UserService {

    boolean authenticate(User user);

    List<UserInfo> getUsers();

    UserInfo getUser(Long telegramId);

    void deleteUser(Long telegramId);

    void saveUserAvatar(Long userId);

    void updateProfilePicture();
}
