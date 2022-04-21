package ru.acuma.k.shuffler.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class KickerTeam {

    public KickerTeam(KickerEventPlayer player1, KickerEventPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = false;
        this.rating = (player1.getRating() + player2.getRating()) / 2;
    }

    private KickerEventPlayer player1;

    private KickerEventPlayer player2;

    private long rating;

    private long ratingChange;

    private boolean winner;

    @Override
    public String toString() {
        return player1.getName() + " %s " + player2.getName();
    }

    public List<KickerEventPlayer> getPlayers() {
        return List.of(player1, player2);
    }
}
