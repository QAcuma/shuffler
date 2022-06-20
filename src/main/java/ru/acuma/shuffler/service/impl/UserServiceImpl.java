package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.mapper.UserMapper;
import ru.acuma.shuffler.service.UserService;
import ru.acuma.shuffler.tables.pojos.UserInfo;
import ru.acuma.shufflerlib.repository.UserRepository;

import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public UserInfo getUser(Long telegramId) {
        return userRepository.get(telegramId);
    }

    @Override
    @SneakyThrows
    public void saveProfilePhotos(Long telegramId, File photo) {
        URL url = new URL(photo.getFileUrl(botToken));
        var blob = url.openStream().readAllBytes();
        userRepository.saveProfilePhoto(telegramId, blob);
    }

    private Boolean hasAccess(Long telegramId) {
        return (userRepository.isActive(telegramId));
    }

    @Override
    public boolean authenticate(User user) {
        if (hasAccess(user.getId())) {
            return true;
        } else if (!userRepository.isBlocked(user.getId())) {
            userRepository.save(userMapper.toUserInfo(user));
            return true;
        }
        return false;
    }
}
