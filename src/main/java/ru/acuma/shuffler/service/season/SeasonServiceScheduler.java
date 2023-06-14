package ru.acuma.shuffler.service.season;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.constant.YearSeason;
import ru.acuma.shuffler.util.TimeMachine;

@Service
@RequiredArgsConstructor
public class SeasonServiceScheduler {

    private static final String HOURLY = "0 0 * ? * *";

    private final SeasonService seasonRepository;
    private final SeasonService seasonService;
    private final BroadcastService broadcastService;

    @Scheduled(cron = HOURLY)
    public void watchSeason() {
        handleSeason();
    }

    private void handleSeason() {
        var liveYearSeason = YearSeason.getByMonthNumber(TimeMachine.localDateNow().getMonthValue());
        var appSeason = seasonRepository.getCurrentSeason();
        if (appSeason != null) {
            var appYearSeason = YearSeason.getByMonthNumber(appSeason.getStartedAt().getMonthValue());
            if (appYearSeason == liveYearSeason) {
                return;
            }
        }

        broadcastService.seasonResultBroadcast(appSeason.getId());
        seasonService.startNewSeason();
    }
}
