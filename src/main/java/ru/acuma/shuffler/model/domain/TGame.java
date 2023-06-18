package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.model.constant.GameStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.acuma.shuffler.model.constant.GameStatus.FINISHED;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class TGame implements Serializable {

    private Long id;
    private Integer index;
    private Integer order;
    private Integer messageId;
    private GameStatus status;
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
        if (status == FINISHED) {
            return redTeam.getIsWinner() ? redTeam : blueTeam;
        }

        return null;
    }

    public TTeam getLoserTeam() {
        if (status == FINISHED) {
            return Boolean.TRUE.equals(redTeam.getIsWinner()) ? blueTeam : redTeam;
        }

        return null;
    }

    public boolean hasSameBonus() {
        return Objects.equals(
            getWinnerTeam().getPlayer1().getRatingContext().getGameHistory().get(id),
            getWinnerTeam().getPlayer2().getRatingContext().getGameHistory().get(id)
        );
    }

    public boolean isActive() {
        return List.of(
            GameStatus.ACTIVE,
            GameStatus.CANCEL_CHECKING,
            GameStatus.EVENT_CHECKING,
            GameStatus.BLUE_CHECKING,
            GameStatus.RED_CHECKING
        ).contains(getStatus());
    }

    public String getGameResult() {
        return hasSameBonus()
               ? getBasicGameResult()
               : getPersonalGameResult();
    }

    private String getPersonalGameResult() {
        var winBet = getWinnerTeam().getBaseBet().getCaseWin();
        return order +
            ". " +
            String.format(getWinnerTeam().toString(), "&") +
            wrapBrackets(
                "+" + getWinnerTeam().getPlayer1().getRatingContext().getGameHistory().get(id) +
                    " / " +
                    "+" + getWinnerTeam().getPlayer2().getRatingContext().getGameHistory().get(id)
            );
    }

    private <T> String wrapBrackets(final T content) {
        return " (" + content + ")";
    }

    private String getBasicGameResult() {
        var winBet = getWinnerTeam().getBaseBet().getCaseWin();
        return order +
            ". " +
            String.format(getWinnerTeam().toString(), "&") +
            " (+" +
            getWinnerTeam().getPlayer1().getRatingContext().getGameHistory().get(id) +
            ")";
    }
}
