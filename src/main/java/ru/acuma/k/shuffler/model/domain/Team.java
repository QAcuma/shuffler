package ru.acuma.k.shuffler.model.domain;

import lombok.Data;

@Data
public class Team {

    public Team(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.rating = (player1.getRating() + player2.getRating()) / 2;
    }

    private Player player1;

    private Player player2;

    private Long rating;

}
