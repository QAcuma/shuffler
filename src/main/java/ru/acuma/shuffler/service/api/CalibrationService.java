package ru.acuma.shuffler.service.api;

public interface CalibrationService {

    int getDistinctEnemiesCount(Long playerId);

    boolean isCalibrated(Long playerId);

}
