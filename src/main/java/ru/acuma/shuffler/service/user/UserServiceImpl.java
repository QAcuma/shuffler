package ru.acuma.shuffler.service.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.UserProfilePhotos;
import ru.acuma.shuffler.mapper.UserMapper;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.UserService;
import ru.acuma.shuffler.tables.pojos.UserInfo;
import ru.acuma.shufflerlib.repository.UserRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ExecuteService executeService;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${application.media.location}")
    private String mediaLocation;

    @Override
    public UserInfo getUser(Long telegramId) {
        return userRepository.get(telegramId);
    }

    @Override
    public void deleteUser(Long telegramId) {
        var user = userRepository.get(telegramId);
        user.setDeletedAt(OffsetDateTime.now());
        userRepository.update(user);
    }

    @SneakyThrows
    private void saveProfilePhotos(Long telegramId, File photo) {
        try {
            URL url = new URL(photo.getFileUrl(botToken));
            ByteArrayInputStream bis = new ByteArrayInputStream(url.openStream().readAllBytes());
            BufferedImage image = ImageIO.read(bis);
            var extension = StringUtils.substringAfterLast(photo.getFilePath(), ".");
            var outputFile = new java.io.File(mediaLocation + photo.getFileUniqueId() + "." + extension);
            ImageIO.write(image, extension, outputFile);
            userRepository.saveProfilePhotoId(telegramId, outputFile.getName());
        } catch (UnknownHostException e) {
            log.info("Failed to save picture for user {}. Cause: {}", telegramId, e.getMessage());
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean authenticate(User user) {
        if (hasAccess(user.getId())) {
            return true;
        } else if (!userRepository.isBlocked(user.getId())) {
            userRepository.save(userMapper.toUserInfo(user));
            return true;
        }
        return false;
    }

    @Override
    public List<UserInfo> getUsers() {
        return userRepository.getAll(Boolean.FALSE);
    }

    @Override
    public void saveUserAvatar(Long userId) {
        Optional.ofNullable(findUserPictures(userId))
                .flatMap(getPhotosFunction())
                .ifPresent(getSizeConsumer(userId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProfilePicture() {
        getUsers().stream()
                .filter(Predicate.not(UserInfo::getIsBot))
                .filter(userInfo -> Objects.isNull(userInfo.getDeletedAt()))
                .map(UserInfo::getTelegramId)
                .forEach(this::saveUserAvatar);
    }

    private Boolean hasAccess(Long telegramId) {
        return userRepository.isActive(telegramId);
    }

    @NotNull
    private Function<UserProfilePhotos, Optional<? extends PhotoSize>> getPhotosFunction() {
        return pictures -> pictures.getPhotos().stream().findFirst().flatMap(picture -> picture.stream().findFirst());
    }

    @NotNull
    private Consumer<PhotoSize> getSizeConsumer(Long userId) {
        return smallPhoto -> {
            var getFile = new GetFile(smallPhoto.getFileId());
            var file = loadUserProfilePicture(getFile);
            saveProfilePhotos(userId, file);
        };
    }

    @SneakyThrows
    private UserProfilePhotos findUserPictures(Long getUserPhotos) {
        var method = new GetUserProfilePhotos(getUserPhotos);

        return executeService.executeApi(method);
    }

    @SneakyThrows
    private File loadUserProfilePicture(GetFile getFile) {
        return executeService.executeApi(getFile);
    }
}
