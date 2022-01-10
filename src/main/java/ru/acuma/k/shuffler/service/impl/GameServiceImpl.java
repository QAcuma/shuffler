package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.model.enums.GameState;
import ru.acuma.k.shuffler.model.enums.WinnerState;
import ru.acuma.k.shuffler.service.GameService;
import ru.acuma.k.shuffler.service.PlayerService;
import ru.acuma.k.shuffler.service.RatingService;
import ru.acuma.k.shuffler.service.ShuffleService;
import ru.acuma.k.shuffler.service.TeamService;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final TeamService teamService;
    private final ShuffleService shuffleService;
    private final RatingService ratingService;
    private final PlayerService playerService;

    @Value("${rating.spread-distance}")
    private long spreadDistance;

    @SneakyThrows
    public KickerGame buildGame(KickerEvent event) {
        List<KickerEventPlayer> players;
        KickerTeam redTeam;
        try {
            players = shuffleService.shuffle(event);
            if (players == null) {
                throw new InstanceNotFoundException("Недостаточно игроков для начала матча");
            }
            redTeam = teamService.teamBuilding(players, spreadDistance);
        } catch (IllegalArgumentException e) {
            return buildGame(event);
        }
        if (redTeam == null) {
            throw new IllegalArgumentException("Red team is null");
        }
        players.removeAll(redTeam.getPlayers());
        KickerTeam blueTeam = teamService.teamBuilding(players, spreadDistance);
        return KickerGame.builder()
                .redTeam(redTeam)
                .blueTeam(blueTeam)
                .index(event.getGames().size() + 1)
                .startedAt(LocalDateTime.now())
                .state(GameState.STARTED)
                .build();
    }

    @Override
    public void endGame(KickerEvent event, WinnerState state) {
        var game = event.getCurrentGame();
        if (game == null) {
            return;
        }
        switch (state) {
            case RED:
                game.getRedTeam().setWinner(true);
                finishGame(event);
                break;
            case BLUE:
                game.getBlueTeam().setWinner(true);
                finishGame(event);
                break;
            case NONE:
                game.setState(GameState.CANCELLED);
                game.setFinishedAt(LocalDateTime.now());
                break;
        }
        game.setFinishedAt(LocalDateTime.now());
    }

    private void finishGame(KickerEvent event) {
        var game = event.getCurrentGame();
        game.setState(GameState.FINISHED);
        ratingService.update(event);
        playerService.updatePlayersRating(event);
        game.getPlayers().forEach(KickerEventPlayer::gg);
        teamService.fillLastGameMate(game.getWinnerTeam());
        teamService.fillLastGameMate(game.getLoserTeam());
    }

}
