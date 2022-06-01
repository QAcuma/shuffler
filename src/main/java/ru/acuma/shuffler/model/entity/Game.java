package ru.acuma.shuffler.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.model.enums.GameState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.acuma.shuffler.model.enums.GameState.FINISHED;

@Getter
@Setter
@Builder
@Accessors(chain = true)
public class Game {

    private Long id;

    private int index;

    private int messageId;

    private GameState state;

    private GameTeam redTeam;

    private GameTeam blueTeam;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    public List<GameEventPlayer> getPlayers() {
        List<GameEventPlayer> players = new ArrayList<>();
        players.addAll(blueTeam.getPlayers());
        players.addAll(redTeam.getPlayers());
        return players;
    }

    public GameTeam getWinnerTeam() {
        if (state == FINISHED) {
            return redTeam.isWinner() ? redTeam : blueTeam;
        }
        return null;
    }

    public GameTeam getLoserTeam() {
        if (state == FINISHED) {
            return redTeam.isWinner() ? blueTeam : redTeam;
        }
        return null;
    }

    public String getGameResult() {
        if (getWinnerTeam() != null) {
            return index +
                    ". " +
                    String.format(getWinnerTeam().toString(), "&") +
                    " (+" +
                    getWinnerTeam().getRatingChange() +
                    ") ";
        }
        return "";
    }

}
