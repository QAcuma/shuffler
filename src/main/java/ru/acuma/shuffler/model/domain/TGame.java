package ru.acuma.shuffler.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@Builder
@Accessors(chain = true)
public class TGame implements Serializable {

    private Long id;
    private Integer index;
    private Integer messageId;
    private GameState state;
    private TTeam redTeam;
    private TTeam blueTeam;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public List<TEventPlayer> getPlayers() {
        List<TEventPlayer> players = new ArrayList<>();
        players.addAll(blueTeam.getPlayers());
        players.addAll(redTeam.getPlayers());

        return players;
    }

    public TTeam getWinnerTeam() {
        if (state == FINISHED) {
            return redTeam.getIsWinner() ? redTeam : blueTeam;
        }

        return null;
    }

    public TTeam getLoserTeam() {
        if (state == FINISHED) {
            return redTeam.getIsWinner() ? blueTeam : redTeam;
        }

        return null;
    }

    public boolean isCalibrating() {
        return getRedTeam().containsCalibrating() || getBlueTeam().containsCalibrating();
    }

    public boolean isActive() {
        return List.of(
            GameState.ACTIVE,
            GameState.CANCEL_CHECKING,
            GameState.BLUE_CHECKING,
            GameState.RED_CHECKING
        ).contains(getState());
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
