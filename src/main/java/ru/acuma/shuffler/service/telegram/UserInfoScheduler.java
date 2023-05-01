package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoScheduler {

    private final ProfilePictureService profilePictureService;

    //    @Scheduled(initialDelay = 0L, fixedDelay = 30_000L)
    public void syncUserInfo() {
        profilePictureService.updateProfilePicture();
    }

}
