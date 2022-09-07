package ru.acuma.shuffler.model.entity;

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

    private int score;

    private boolean isCalibrated;

    private int calibrationMultiplier;

    public int getScoreSorting() {
        return isCalibrated ? score : 0;
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

    public int applyRating(int changes) {
        var applied = changes * getCalibrationMultiplier();
        score += applied;

        return applied;
    }

    public int getCalibrationMultiplier() {
        return isCalibrated ? 1 : calibrationMultiplier;
    }

}
