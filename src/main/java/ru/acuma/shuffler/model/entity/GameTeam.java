package ru.acuma.shuffler.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class GameTeam {

    private Long id;

    private GameEventPlayer player1;

    private GameEventPlayer player2;

    private long rating;

    private long ratingChange;

    private boolean isWinner;

    public GameTeam(GameEventPlayer player1, GameEventPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.isWinner = false;
        this.rating = (player1.getRating() + player2.getRating()) / 2;
    }

    @Override
    public String toString() {
        return player1.getName() + " %s " + player2.getName();
    }

    public List<GameEventPlayer> getPlayers() {
        return List.of(player1, player2);
    }
}
