package ru.acuma.k.shuffler.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.acuma.k.shuffler.model.enums.GameState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.acuma.k.shuffler.model.enums.GameState.FINISHED;

@Getter
@Setter
@Builder
@Accessors(chain = true)
public class KickerGame {

    private Long id;

    private int index;

    private int messageId;

    private GameState state;

    private KickerTeam redTeam;

    private KickerTeam blueTeam;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    public List<KickerEventPlayer> getPlayers() {
        List<KickerEventPlayer> players = new ArrayList<>();
        players.addAll(blueTeam.getPlayers());
        players.addAll(redTeam.getPlayers());
        return players;
    }

    public KickerTeam getWinnerTeam() {
        if (state == FINISHED) {
            return redTeam.isWinner() ? redTeam : blueTeam;
        }
        return null;
    }

    public KickerTeam getLoserTeam() {
        if (state == FINISHED) {
            return redTeam.isWinner() ? blueTeam : redTeam;
        }
        return null;
    }

    public String getGameResult() {
        if (getWinnerTeam() != null) {
            return  index +
                    ". " +
                    getWinnerTeam().toString() +
                    " (+" +
                    getWinnerTeam().getRatingChange() +
                    ") ";
        }
        return "";
    }

}
