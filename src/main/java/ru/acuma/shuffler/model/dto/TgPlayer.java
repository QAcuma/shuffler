package ru.acuma.shuffler.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.tables.pojos.UserInfo;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
public class TgPlayer implements Serializable {

    private Long id;
    private Long chatId;
    private int score;
    private boolean calibrated;
    private int calibrationMultiplier;
    private TgUserInfo userInfo;

    public int getScoreSorting() {
        return calibrated ? score : 0;
    }

    public String getScoreString() {
        return calibrated ? String.valueOf(score) : "calibrating";
    }

    public String getName() {
        return userInfo.getFirstName() +
                " " +
                Optional.ofNullable(userInfo.getLastName()).orElse("Doe") +
                this.getScoreString();
    }

    public int applyRating(int changes) {
        var applied = changes * getCalibrationMultiplier();
        score += applied;

        return applied;
    }

    public int getCalibrationMultiplier() {
        return calibrated ? 1 : calibrationMultiplier;
    }

}
