package ru.acuma.k.shuffler.model.domain;

import lombok.Data;

@Data
public class Team {

    public Team(EventPlayer player1, EventPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.rating = (player1.getRating() + player2.getRating()) / 2;
    }

    private EventPlayer player1;

    private EventPlayer player2;

    private Long rating;

}
