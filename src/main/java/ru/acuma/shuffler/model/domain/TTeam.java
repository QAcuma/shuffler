package ru.acuma.shuffler.model.domain;

import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

import static ru.acuma.shuffler.util.Symbols.MEDAL_EMOJI;

@Data
@Builder
@Accessors(chain = true)
public class TTeam implements Serializable {

    private Long id;
    private TEventPlayer player1;
    private TEventPlayer player2;
    private Integer score;
    private Boolean isWinner;
    private TGameBet bet;

    public void applyRating() {
        var change = getIsWinner() ? bet.getCaseWin() : bet.getCaseLose();
        getPlayers().forEach(player -> player.applyRating(change));
    }

    public String getBetText() {
        return MEDAL_EMOJI + "+" + bet.getCaseWin();
    }

    public String getScoreString() {
        return containsCalibrating() ? "calibrating" : String.valueOf(score);
    }

    public boolean containsCalibrating() {
        return getPlayers().stream()
            .anyMatch(Predicate.not(TEventPlayer::isCalibrated));
    }

    public List<TEventPlayer> getPlayers() {
        return List.of(player1, player2);
    }

    @Override
    public String toString() {
        return player1.getName() + " %s " + player2.getName();
    }
}
