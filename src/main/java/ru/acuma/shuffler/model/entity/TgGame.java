package ru.acuma.shuffler.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.util.Symbols;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.acuma.shuffler.model.enums.GameState.FINISHED;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class TgGame implements Serializable {

    private Long id;
    private int index;
    private int messageId;
    private GameState state;
    private TgTeam redTeam;
    private TgTeam blueTeam;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public List<TgEventPlayer> getPlayers() {
        List<TgEventPlayer> players = new ArrayList<>();
        players.addAll(blueTeam.getPlayers());
        players.addAll(redTeam.getPlayers());

        return players;
    }

    public TgTeam getWinnerTeam() {
        if (state == FINISHED) {
            return redTeam.isWinner() ? redTeam : blueTeam;
        }

        return null;
    }

    public TgTeam getLoserTeam() {
        if (state == FINISHED) {
            return redTeam.isWinner() ? blueTeam : redTeam;
        }

        return null;
    }

    public boolean isCalibrating() {
        return getRedTeam().containsCalibrating() || getBlueTeam().containsCalibrating();
    }

    public String getGameResult() {
        if (getWinnerTeam() != null) {
            return index +
                    ". " +
                    String.format(getWinnerTeam().toString(), "&") +
                    " (+" +
                    getWinnerTeam().getBet().getCaseWin() +
                    ") " +
                    (isCalibrating() ? Symbols.FOOTNOTE_SYMBOL : "");
        }
        return "";
    }

}
