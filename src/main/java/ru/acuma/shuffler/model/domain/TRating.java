package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class TRating implements Serializable {

    private Long id;
    private Integer score;
    private Integer eventScoreChange;
    private Integer lastScoreChange;
    private BigDecimal multiplier;

    public void applyScore(final Integer change) {
        var scoreChange = multiplier.multiply(BigDecimal.valueOf(change)).intValue();
        if (multiplier.compareTo(BigDecimal.ONE) > 0) {
            multiplier = multiplier.subtract(BigDecimal.valueOf(0.5d));
        }
        score += scoreChange;
        eventScoreChange = eventScoreChange + scoreChange;
        lastScoreChange = scoreChange;
    }
}
