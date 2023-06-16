package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class TRating implements Serializable {

    private Long id;
    private Boolean calibrated;
    private Integer score;
    private Integer eventScoreChange;
    private Integer lastScoreChange;
    private Integer calibrationMultiplier;

    public void applyScore(Integer change) {
        var scoreChange = change * calibrationMultiplier;
        score += scoreChange;

        eventScoreChange = getCalibrated()
                           ? eventScoreChange + scoreChange
                           : 0;
        lastScoreChange = scoreChange;
    }
}
