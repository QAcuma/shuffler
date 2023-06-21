package ru.acuma.shuffler.service.season;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.model.constant.YearSeason;
import ru.acuma.shuffler.model.entity.Season;
import ru.acuma.shuffler.repository.ReferenceService;
import ru.acuma.shuffler.repository.SeasonRepository;
import ru.acuma.shuffler.util.TimeMachine;

import static ru.acuma.shuffler.config.CacheConfig.SEASON_ID;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final ReferenceService referenceService;
    private final SeasonRepository seasonRepository;
    @Lazy
    private final SeasonService self;
    private final BroadcastService broadcastService;

    @Transactional
    public Season getCurrentSeason() {
        return seasonRepository.findByFinishedAtIsNull()
            .orElseGet(this::startNewSeason);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Season getReference() {
        return referenceService.getReference(Season.class, getSeasonId());
    }

    @Transactional
    @Cacheable(value = SEASON_ID)
    public Long getSeasonId() {
        return getCurrentSeason().getId();
    }

    public Season startNewSeason() {
        var date = TimeMachine.localDateNow();
        var season = Season.builder()
            .startedAt(TimeMachine.localDateTimeNow())
            .name(YearSeason.getByMonthNumber(date.getMonthValue()).toString() + date.getYear())
            .build();

        return seasonRepository.save(season);
    }

    @Transactional
    public void handleSeason() {
        var appSeason = getCurrentSeason();
        if (appSeason != null) {
            var liveYearSeason = YearSeason.getByMonthNumber(TimeMachine.localDateNow().getMonthValue());
            var appYearSeason = YearSeason.getByMonthNumber(appSeason.getStartedAt().getMonthValue());
            if (appYearSeason == liveYearSeason) {
                return;
            }
            broadcastService.seasonResultBroadcast(appSeason);
            self.finishSeason(appSeason);
            self.startNewSeason();
        }

    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void finishSeason(final Season oldSeason) {
        oldSeason.setFinishedAt(TimeMachine.localDateTimeNow());
    }
}
