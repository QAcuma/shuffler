package ru.acuma.shuffler.service.api;

import ru.acuma.shufflerlib.model.Discipline;

public interface CalibrationService {

    int getDistinctEnemiesCount(Long playerId);

    boolean isCalibrated(Long playerId, Discipline discipline);

}
