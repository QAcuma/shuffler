package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.mapper.UserMapper;
import ru.acuma.shuffler.service.api.UserService;
import ru.acuma.shuffler.tables.pojos.UserInfo;
import ru.acuma.shufflerlib.repository.UserRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.UnknownHostException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${application.media.location}")
    private String mediaLocation;

    @Override
    public UserInfo getUser(Long telegramId) {
        return userRepository.get(telegramId);
    }

    @Override
    @SneakyThrows
    public void saveProfilePhotos(Long telegramId, File photo) {
        try {
            URL url = new URL(photo.getFileUrl(botToken));
            ByteArrayInputStream bis = new ByteArrayInputStream(url.openStream().readAllBytes());
            BufferedImage image = ImageIO.read(bis);
            var outputFile = new java.io.File(mediaLocation + photo.getFileUniqueId() + ".png");

            ImageIO.write(image, "png", outputFile);
            userRepository.saveProfilePhotoId(telegramId, outputFile.getName());
        } catch (UnknownHostException e) {
            log.info("Failed to save picture for user {}", telegramId);
        }
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
