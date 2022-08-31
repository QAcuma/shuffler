package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.tables.pojos.Season;

public interface SeasonService {

    Season getCurrentSeason();

    Season evictSeason();

    void invalidateSeason();

}
