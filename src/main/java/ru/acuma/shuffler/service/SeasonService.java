package ru.acuma.shuffler.service;

import ru.acuma.shuffler.tables.pojos.Season;

public interface SeasonService {

    Season getCurrentSeason();

    void invalidateSeason();

}
