package ru.acuma.shuffler.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Optional;

@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class TgPlayer implements Serializable {

    private Long id;
    private Long chatId;
    private Integer score;
    private Boolean calibrated;
    private Integer calibrationMultiplier;
    private TgUserInfo userInfo;

    public Integer getScoreSorting() {
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

    public Integer applyRating(int changes) {
        var applied = changes * getCalibrationMultiplier();
        score += applied;

        return applied;
    }

    public Integer getCalibrationMultiplier() {
        return calibrated ? 1 : calibrationMultiplier;
    }

}
