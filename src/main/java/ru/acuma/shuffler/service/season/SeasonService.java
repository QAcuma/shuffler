package ru.acuma.shuffler.service.season;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.entity.Season;
import ru.acuma.shuffler.repository.SeasonRepository;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonRepository seasonRepository;

    private Season season;

    public Season getCurrentSeason() {
        return season == null
               ? setSeason()
               : season;
    }

    public Season evictSeason() {
        return season = null;
    }

    public void invalidateSeason() {
        season = null;
    }

    private Season setSeason() {
        season = seasonRepository.findByFinishedAtIsNull()
            .orElseGet(() -> startNewSeason());
        return season;
    }

    public Season startNewSeason() {
        return new Season();
    }
}
