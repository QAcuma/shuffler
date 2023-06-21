package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
    private final Map<Long, Integer> gameHistory = new HashMap<>();

    public void applyScore(final Integer change) {
        var scoreChange = multiplier.multiply(BigDecimal.valueOf(change)).intValue();
        if (multiplier.compareTo(BigDecimal.ONE) > 0) {
            multiplier = multiplier.subtract(BigDecimal.valueOf(0.2d));
        }

        log.debug("baseChange {}, change {}, multiplier {}", change, scoreChange, multiplier);
        score += scoreChange;
        eventScoreChange = eventScoreChange + scoreChange;
        lastScoreChange = scoreChange;
    }
}
