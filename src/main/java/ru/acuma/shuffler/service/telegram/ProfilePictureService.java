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
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.UserProfilePhotos;
import ru.acuma.shuffler.config.properties.BotProperties;
import ru.acuma.shuffler.model.entity.UserInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfilePictureService {

    private final UserService userService;
    private final BotProperties botProperties;

    @Value("${application.media.location}")
    private String mediaLocation;

    @Transactional
    public void updateProfilePicture() {
        userService.getActiveUsers().stream()
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
    private void saveProfilePhotos(Long userId, File photo) {
        try {
            URL url = new URL(photo.getFileUrl(botProperties.getToken()));
            ByteArrayInputStream bis = new ByteArrayInputStream(url.openStream().readAllBytes());
            BufferedImage image = ImageIO.read(bis);
            var extension = StringUtils.substringAfterLast(photo.getFilePath(), ".");
            var outputFile = new java.io.File(mediaLocation + photo.getFileUniqueId() + "." + extension);
            ImageIO.write(image, extension, outputFile);
            var user = userService.getUser(userId);
            user.setMediaId(outputFile.getName());
        } catch (UnknownHostException e) {
            log.info("Failed to save picture for user {}. Cause: {}", userId, e.getMessage());
        }
    }

    @SneakyThrows
    private UserProfilePhotos findUserPictures(Long getUserPhotos) {
        var method = new GetUserProfilePhotos(getUserPhotos);

//        return executeService.executeApi(method);
        return null;
    }

    @SneakyThrows
    private File loadUserProfilePicture(GetFile getFile) {
//        return executeService.executeApi(getFile);
        return null;
    }

}
