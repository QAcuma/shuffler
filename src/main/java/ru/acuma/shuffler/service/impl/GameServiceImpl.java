package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.GameMapper;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TgGame;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.model.enums.WinnerState;
import ru.acuma.shuffler.service.GameService;
import ru.acuma.shuffler.service.RatingService;
import ru.acuma.shuffler.service.ShuffleService;
import ru.acuma.shuffler.service.TeamService;
import ru.acuma.shufflerlib.repository.GameRepository;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final TeamService teamService;
    private final ShuffleService shuffleService;
    private final RatingService ratingService;
    private final GameMapper gameMapper;
    private final GameRepository gameRepository;

    @SneakyThrows
    public TgGame buildGame(TgEvent event) {
        var players = Optional
                .ofNullable(shuffleService.shuffle(event))
                .orElseThrow(() -> new InstanceNotFoundException("Not enough players to start"));
        var redTeam = Optional
                .ofNullable(teamService.buildTeam(players))
                .orElseThrow(() -> new IllegalArgumentException("Red team is null"));

        var secondTeamPlayers = players.stream()
                .filter(Predicate.not(redTeam.getPlayers()::contains))
                .collect(Collectors.toList());
        var blueTeam = teamService.buildTeam(secondTeamPlayers);

        ratingService.applyBet(redTeam, blueTeam);

        var game = new TgGame()
                .setRedTeam(redTeam)
                .setBlueTeam(blueTeam)
                .setIndex(event.getTgGames().size() + 1)
                .setStartedAt(LocalDateTime.now())
                .setState(GameState.STARTED);

        return save(game, event.getId());
    }

    private TgGame save(TgGame tgGame, Long eventId) {
        var mappedGame = gameMapper.toGame(tgGame).setEventId(eventId);
        tgGame.setId(gameRepository.save(mappedGame));
        teamService.save(tgGame.getBlueTeam(), tgGame.getId());
        teamService.save(tgGame.getRedTeam(), tgGame.getId());

        return tgGame;
    }

    @Override
    public void endGame(TgEvent event, WinnerState state) {
        var game = event.getCurrentGame();
        if (game == null) {
            return;
        }
        switch (state) {
            case RED:
                game.getRedTeam().setWinner(true);
                finishGameWithWinner(game);
                break;
            case BLUE:
                game.getBlueTeam().setWinner(true);
                finishGameWithWinner(game);
                break;
            case NONE:
                finishCancelledGame(game);
                break;
        }
        game.setFinishedAt(LocalDateTime.now());
        saveGameData(event);
    }

    private void finishGameWithWinner(TgGame game) {
        game.setState(GameState.FINISHED);
        teamService.fillLastGameMate(game.getWinnerTeam());
        teamService.fillLastGameMate(game.getLoserTeam());
    }

    private void finishCancelledGame(TgGame game) {
        game.setState(GameState.CANCELLED);
    }

    private void saveGameData(TgEvent event) {
        var game = event.getCurrentGame();
        switch (game.getState()) {
            case CANCELLED:
                break;
            case FINISHED:
                ratingService.update(event);
                game.getPlayers().forEach(TgEventPlayer::increaseGameCount);
                teamService.update(game.getWinnerTeam());
        }
        gameRepository.update(gameMapper.toGame(game));
    }

}
