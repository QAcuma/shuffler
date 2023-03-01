package ru.acuma.shuffler.service.game;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.GameMapper;
import ru.acuma.shuffler.model.dto.TgEvent;
import ru.acuma.shuffler.model.dto.TgEventPlayer;
import ru.acuma.shuffler.model.dto.TgGame;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.service.api.GameService;
import ru.acuma.shuffler.service.api.RatingService;
import ru.acuma.shuffler.service.api.ShuffleService;
import ru.acuma.shuffler.service.api.TeamService;
import ru.acuma.shufflerlib.repository.GameRepository;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final TeamService teamService;
    private final ShuffleService shuffleService;
    private final RatingService ratingService;
    private final GameMapper gameMapper;
    private final GameRepository gameRepository;

    @SneakyThrows
    private TgGame buildGame(TgEvent event) {
        var players = Optional
                .ofNullable(shuffleService.shuffle(event))
                .orElseThrow(() -> new InstanceNotFoundException("Not enough players to start"));
        var redTeam = Optional
                .ofNullable(teamService.buildTeam(players))
                .orElseThrow(() -> new IllegalArgumentException("Red team is null"));

        var secondTeamPlayers = players.stream()
                .filter(Predicate.not(redTeam.getPlayers()::contains))
                .toList();
        var blueTeam = teamService.buildTeam(secondTeamPlayers);

        ratingService.applyBet(redTeam, blueTeam);

        return new TgGame()
                .setRedTeam(redTeam)
                .setBlueTeam(blueTeam)
                .setIndex(event.getTgGames().size() + 1)
                .setStartedAt(LocalDateTime.now())
                .setState(GameState.ACTIVE);
    }

    private TgGame save(TgGame tgGame, Long eventId) {
        var mappedGame = gameMapper.toGame(tgGame).setEventId(eventId);
        tgGame.setId(gameRepository.save(mappedGame));
        teamService.save(tgGame.getBlueTeam(), tgGame.getId());
        teamService.save(tgGame.getRedTeam(), tgGame.getId());

        return tgGame;
    }

    @Override
    public void nextGame(TgEvent event) {
        var game = buildGame(event);
        save(game, event.getId());
        event.applyGame(game);
    }

    @Override
    public void handleGameCheck(TgEvent event) {
        var game = event.getLatestGame();
        switch (game.getState()) {
            case RED_CHECKING -> {
                game.getRedTeam().setWinner(true);
                finishGameWithWinner(game);
            }
            case BLUE_CHECKING -> {
                game.getBlueTeam().setWinner(true);
                finishGameWithWinner(game);
            }
            case CANCEL_CHECKING -> finishCancelledGame(game);

        }
        saveGameData(event);
    }

    private void finishGameWithWinner(TgGame game) {
        game.setState(GameState.FINISHED)
                .setFinishedAt(LocalDateTime.now());
        teamService.fillLastGameMate(game.getWinnerTeam());
        teamService.fillLastGameMate(game.getLoserTeam());
    }

    private void finishCancelledGame(TgGame game) {
        game.setState(GameState.CANCELLED)
                .setFinishedAt(LocalDateTime.now());
    }

    private void saveGameData(TgEvent event) {
        var game = event.getLatestGame();
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
