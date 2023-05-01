package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.UserProfilePhotos;
import ru.acuma.shuffler.config.properties.BotProperties;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.UserMapper;
import ru.acuma.shuffler.model.constant.AuthStatus;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.entity.UserInfo;
import ru.acuma.shuffler.repository.UserInfoRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements Authenticatable<Long> {
    private final UserInfoRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserInfo getUser(final Long telegramId) {
        return userRepository.findById(telegramId)
            .orElseThrow(() -> new DataException(ExceptionCause.USER_NOT_FOUND, telegramId));
    }

    @Transactional
    public void signUp(final User user) {
        var userInfo = userMapper.toUserInfo(user);
        userRepository.save(userInfo);
    }

    @Transactional
    public void update(final User user) {
        var userInfo = getUser(user.getId());
        userMapper.mergeUserInfo(userInfo, user);
    }

    @Transactional(readOnly = true)
    public List<UserInfo> getActiveUsers() {
        return userRepository.findAllByIsActiveTrue();
    }

    @Transactional
    public void deleteUser(Long telegramId) {
        getUser(telegramId).setDeletedAt(OffsetDateTime.now(ZoneId.of("Europe/Moscow")));
    }

    @Transactional
    public boolean signIn(User user) {
        var userInfo = getUser(user.getId());
        userMapper.mergeUserInfo(userInfo, user);

        return userInfo.getIsActive();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthStatus authenticate(final Long telegramId) {
        return userRepository.findById(telegramId)
            .map(info -> Boolean.TRUE.equals(info.getIsActive())
                         ? AuthStatus.SUCCESS
                         : AuthStatus.DENY)
            .orElse(AuthStatus.UNREGISTERED);
    }
}
