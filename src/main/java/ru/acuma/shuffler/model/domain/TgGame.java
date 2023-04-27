package ru.acuma.shuffler.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import ru.acuma.shuffler.model.constant.GameState;
import ru.acuma.shuffler.util.Symbols;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.acuma.shuffler.model.constant.GameState.FINISHED;

@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class TgGame implements Serializable {

    private Long id;
    private Integer index;
    private Integer messageId;
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
            return redTeam.getIsWinner() ? redTeam : blueTeam;
        }

        return null;
    }

    public TgTeam getLoserTeam() {
        if (state == FINISHED) {
            return redTeam.getIsWinner() ? blueTeam : redTeam;
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
