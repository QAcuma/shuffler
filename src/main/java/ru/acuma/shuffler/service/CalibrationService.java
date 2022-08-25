package ru.acuma.shuffler.service;

public interface CalibrationService {

    int getDistinctEnemiesCount(Long playerId);

    boolean isCalibrated(Long playerId);

}
