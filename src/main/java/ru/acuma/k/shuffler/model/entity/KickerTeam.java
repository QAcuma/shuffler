package ru.acuma.k.shuffler.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class KickerTeam {

    public KickerTeam(KickerEventPlayer player1, KickerEventPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.rating = (player1.getRating() + player2.getRating()) / 2;
    }

    private KickerEventPlayer player1;

    private KickerEventPlayer player2;

    private int rating;

    private int ratingChange;

    @Override
    public String toString() {
        return player1.getName() + " & " + player2.getName();
    }
}
