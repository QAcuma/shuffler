package ru.acuma.shuffler.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Predicate;

@Getter
@Setter
@Accessors(chain = true)
public class TgTeam {

    private Long id;

    private TgEventPlayer player1;

    private TgEventPlayer player2;

    private long score;

    private int ratingChange;

    private boolean isWinner;

    public TgTeam(TgEventPlayer player1, TgEventPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.isWinner = false;
        this.score = (player1.getScore() + player2.getScore()) / 2;
    }

    public boolean containsCalibrating() {
        return getPlayers().stream().anyMatch(Predicate.not(TgEventPlayer::isCalibrated));
    }

    @Override
    public String toString() {
        return player1.getName() + " %s " + player2.getName();
    }

    public List<TgEventPlayer> getPlayers() {
        return List.of(player1, player2);
    }
}
