package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class TTeam implements Serializable {

    private Long id;
    private TEventPlayer player1;
    private TEventPlayer player2;
    private Integer score;
    private Boolean isWinner;
    private TGameBet baseBet;

    public void applyRating() {
        var change = getIsWinner() ? baseBet.getCaseWin() : baseBet.getCaseLose();
        getPlayers().forEach(player -> player.applyRating(change));
    }

    public List<TEventPlayer> getPlayers() {
        return List.of(player1, player2);
    }

    @Override
    public String toString() {
        return player1.getName() + " %s " + player2.getName();
    }
}
