package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.model.enums.GameState;
import ru.acuma.k.shuffler.model.enums.WinnerState;
import ru.acuma.k.shuffler.service.*;
import ru.acuma.k.shuffler.tables.pojos.Game;
import ru.acuma.shufflerlib.dao.GameDao;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final TeamService teamService;
    private final ShuffleService shuffleService;
    private final RatingService ratingService;
    private final PlayerService playerService;
    private final GameDao gameDao;

    @SneakyThrows
    public KickerGame buildGame(KickerEvent event) {
        List<KickerEventPlayer> players;
        KickerTeam redTeam;
        KickerTeam blueTeam;
        try {
            players = shuffleService.shuffle(event);
            if (players == null) {
                throw new InstanceNotFoundException("Not enough players to start");
            }
            redTeam = teamService.teamBuilding(players);
            if (redTeam == null) {
                throw new IllegalArgumentException("Red team is null");
            }
            List<KickerEventPlayer> secondTeamPlayers = players.stream()
                    .filter(player -> !redTeam.getPlayers().contains(player))
                    .collect(Collectors.toList());
            blueTeam = teamService.teamBuilding(secondTeamPlayers);
        } catch (IllegalArgumentException e) {
            return buildGame(event);
        }
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
