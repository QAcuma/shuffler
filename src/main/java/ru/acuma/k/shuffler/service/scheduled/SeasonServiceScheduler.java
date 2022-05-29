package ru.acuma.k.shuffler.service.scheduled;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.acuma.shufflerlib.dao.SeasonDao;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class SeasonServiceScheduler {

    private final SeasonDao seasonDao;

    private final static String hourly = "15 */1 * * * *";

    @Scheduled(cron = hourly)
    public void watchSeason() {
        YearSeason liveYearSeason = YearSeason.getByMonthNumber(OffsetDateTime.now().getMonthValue());
        var appSeason = seasonDao.getCurrentSeason();
        if (appSeason != null) {
            YearSeason appYearSeason = YearSeason.getByMonthNumber(appSeason.getStartedAt().getMonthValue());
            if (appYearSeason == liveYearSeason) {
                return;
            }
        }
        seasonDao.startNewSeason(liveYearSeason.name() + OffsetDateTime.now().getYear());
    }

    @Getter
    @RequiredArgsConstructor
    private enum YearSeason {
        WINTER(List.of(12, 1, 2)),
        SPRING(List.of(3, 4, 5)),
        SUMMER(List.of(6, 7, 8)),
        AUTUMN(List.of(9, 10, 11));

        private final List<Integer> months;

        public static YearSeason getByMonthNumber(Integer monthNumber) {
            return Arrays.stream(values())
                    .filter(month -> month.getMonths().contains(monthNumber))
                    .findFirst()
                    .orElseThrow(() -> new EnumConstantNotPresentException(YearSeason.class, String.valueOf(monthNumber)));
        }
    }

}
