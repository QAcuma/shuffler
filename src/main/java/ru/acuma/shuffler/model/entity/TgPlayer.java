package ru.acuma.shuffler.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.tables.pojos.UserInfo;

import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
public class TgPlayer extends UserInfo {

    private Long id;

    private Long chatId;

    @Getter(AccessLevel.NONE)
    private int score;

    private boolean isCalibrated;

    private int calibrationMultiplier;

    public int getScore() {
        return score;
    }

    public int getScoreSorting() {
        return isCalibrated ? score : -1;
    }

    public String getScoreString() {
        return isCalibrated ? String.valueOf(score) : "calibrating";
    }

    public String getName() {
        return super.getFirstName() +
                " " +
                Optional.ofNullable(super.getLastName()).orElse("Doe") +
                this.getScoreString();
    }

    public void plusRating(int value) {
        score += (value * getCalibrationMultiplier());
    }

    public void minusRating(int value) {
        score -= (value * getCalibrationMultiplier());
    }

    private int getCalibrationMultiplier() {
        return isCalibrated ? 1 : calibrationMultiplier;
    }

}
