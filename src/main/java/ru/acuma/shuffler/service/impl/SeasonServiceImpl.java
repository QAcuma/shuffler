package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.service.SeasonService;
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
    public void invalidateSeason() {
        season = null;
    }

    private Season setSeason() {
        season = seasonRepository.getCurrentSeason();
        return season;
    }
}
