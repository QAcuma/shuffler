package ru.acuma.shuffler.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.service.api.UserService;

@Component
@RequiredArgsConstructor
public class UserInfoScheduler {

    private final UserService userService;

    //    @Scheduled(initialDelay = 0L, fixedDelay = 30_000L)
    public void syncUserInfo() {
        userService.updateProfilePicture();
    }

}
