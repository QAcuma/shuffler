package ru.acuma.shuffler.service.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.UserProfilePhotos;
import ru.acuma.shuffler.config.properties.BotProperties;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.UserMapper;
import ru.acuma.shuffler.model.entity.UserInfo;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.repository.UserInfoRepository;
import ru.acuma.shuffler.service.api.ExecuteService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserInfoRepository userRepository;
    private final UserMapper userMapper;
    private final ExecuteService executeService;
    private final BotProperties botProperties;

    @Value("${application.media.location}")
    private String mediaLocation;

    @Transactional(readOnly = true)
    public UserInfo getUser(Long telegramId) {
        return userRepository.findById(telegramId)
            .orElseThrow(() -> new DataException(ExceptionCause.USER_NOT_FOUND, telegramId));
    }

    @Transactional(readOnly = true)
    public List<UserInfo> getActiveUsers() {
        return userRepository.findAllByIsActiveTrue();
    }

    @Transactional
    public void deleteUser(Long telegramId) {
        getUser(telegramId).setDeletedAt(OffsetDateTime.now());
    }

    @Transactional
    public boolean signIn(User user) {
        var userInfo = getUser(user.getId());
        userMapper.mergeUserInfo(userInfo, user);

        return userInfo.getIsActive();
    }

    @Transactional
    public void updateProfilePicture() {
        getActiveUsers().stream()
            .filter(Predicate.not(UserInfo::getIsBot))
            .filter(userInfo -> Objects.isNull(userInfo.getDeletedAt()))
            .map(UserInfo::getId)
            .forEach(this::saveUserAvatar);
    }

    private void saveUserAvatar(Long userId) {
        Optional.ofNullable(findUserPictures(userId))
            .flatMap(this::getPhotosFunction)
            .ifPresent(photo -> getSizeConsumer(userId, photo));
    }

    private Optional<PhotoSize> getPhotosFunction(UserProfilePhotos pictures) {
        return pictures.getPhotos().stream()
            .findFirst()
            .flatMap(picture -> picture.stream().findFirst());
    }

    private void getSizeConsumer(Long userId, PhotoSize picture) {
        var getFile = new GetFile(picture.getFileId());
        var file = loadUserProfilePicture(getFile);

        saveProfilePhotos(userId, file);
    }

    @SneakyThrows
    private void saveProfilePhotos(Long telegramId, File photo) {
        try {
            URL url = new URL(photo.getFileUrl(botProperties.getToken()));
            ByteArrayInputStream bis = new ByteArrayInputStream(url.openStream().readAllBytes());
            BufferedImage image = ImageIO.read(bis);
            var extension = StringUtils.substringAfterLast(photo.getFilePath(), ".");
            var outputFile = new java.io.File(mediaLocation + photo.getFileUniqueId() + "." + extension);
            ImageIO.write(image, extension, outputFile);
            var user = getUser(telegramId);
            user.setMediaId(outputFile.getName());
        } catch (UnknownHostException e) {
            log.info("Failed to save picture for user {}. Cause: {}", telegramId, e.getMessage());
        }
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
