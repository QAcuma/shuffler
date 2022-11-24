package ru.acuma.shuffler.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.service.api.UserService;
import ru.acuma.shuffler.tables.pojos.UserInfo;

import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class UserInfoScheduler {

    private final UserService userService;

    @Scheduled(initialDelay = 30_000L, fixedDelay = 30_000L)
    public void syncUserInfo() {
        updateProfilePicture();
    }

    public void updateProfilePicture() {
        userService.getUsers().stream()
                .filter(Predicate.not(UserInfo::getIsBot))
                .map(UserInfo::getTelegramId)
                .forEach(userService::saveUserAvatar);
    }

}
