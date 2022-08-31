package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.tables.pojos.UserInfo;

public interface UserService {

    UserInfo getUser(Long telegramId);

    void saveProfilePhotos(Long telegramId, File photo);

    boolean authenticate(User user);
}
