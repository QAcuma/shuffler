package ru.acuma.shuffler.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Predicate;

import static ru.acuma.shuffler.util.Symbols.MEDAL_EMOJI;

@Getter
@Setter
@Accessors(chain = true)
public class TgTeam {

    private Long id;
    private TgEventPlayer player1;
    private TgEventPlayer player2;
    private int score;
    private boolean isWinner;
    private TgGameBet bet;

    public TgTeam(TgEventPlayer player1, TgEventPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.isWinner = false;
        this.score = (player1.getScore() + player2.getScore()) / 2;
    }

    @Override
    public String toString() {
        return player1.getName() + " %s " + player2.getName();
    }

    public void applyRating() {
        var change = isWinner() ? bet.getCaseWin() : bet.getCaseLose();
        getPlayers().forEach(player -> player.applyRating(change));
    }

    public String getBetText() {
        return MEDAL_EMOJI + "+" + bet.getCaseWin();
    }

    public String getScoreString() {
        return containsCalibrating() ? "calibrating" : String.valueOf(score);
    }

    public boolean containsCalibrating() {
        return getPlayers().stream().anyMatch(Predicate.not(TgEventPlayer::isCalibrated));
    }

    public List<TgEventPlayer> getPlayers() {
        return List.of(player1, player2);
    }
}
