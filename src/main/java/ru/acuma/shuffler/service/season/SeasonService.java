package ru.acuma.shuffler.service.season;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.model.constant.YearSeason;
import ru.acuma.shuffler.model.entity.Season;
import ru.acuma.shuffler.repository.SeasonRepository;
import ru.acuma.shuffler.util.TimeMachine;

import static ru.acuma.shuffler.config.CacheConfig.SEASON_ID;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonRepository seasonRepository;

    @Transactional
    public Season getCurrentSeason() {
        return seasonRepository.findByFinishedAtIsNull()
            .orElseGet(this::startNewSeason);
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
}
