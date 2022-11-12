package ru.acuma.shuffler.service.season;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.service.api.SeasonService;
import ru.acuma.shuffler.tables.pojos.Season;
import ru.acuma.shufflerlib.repository.SeasonRepository;

@Service
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {

    private final SeasonRepository seasonRepository;

    private Season season;

    @Override
    public Season getCurrentSeason() {
        return season == null ? setSeason() : season;
    }

    @Override
    public Season evictSeason() {
        return season = null;
    }

    @Override
    public void invalidateSeason() {
        season = null;
    }

    private Season setSeason() {
        season = seasonRepository.getCurrentSeason();
        return season;
    }
}
