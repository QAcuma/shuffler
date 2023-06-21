package ru.acuma.shuffler.service.extentions;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.service.season.SeasonService;
import ru.acuma.shuffler.service.telegram.ProfilePictureService;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private static final String HOURLY = "0 0 * ? * *";
    private static final String SOMETIME = "0 0 * ? * *";

    private final SeasonService seasonService;

    @Scheduled(cron = HOURLY)
    public void watchSeason() {
        seasonService.handleSeason();
    }

    private final ProfilePictureService profilePictureService;

//    @Scheduled(initialDelay = 0L, fixedDelay = 30_000L)
    public void downloadProfilePhoto() {
        profilePictureService.updateProfilePicture();
    }
}
