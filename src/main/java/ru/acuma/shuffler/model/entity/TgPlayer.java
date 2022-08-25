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

    public String getName() {
        return super.getFirstName() +
                " " +
                Optional.ofNullable(super.getLastName()).orElse("Doe") +
                score;
    }

    public void plusRating(int value) {
        score += (value * getCalibrationModifier());
    }

    public void minusRating(int value) {
        score -= (value * getCalibrationModifier());
    }

    private int getCalibrationModifier() {
        return isCalibrated ? 1 : 3;
    }

}
