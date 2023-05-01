package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.GameMapper;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.constant.GameState;
import ru.acuma.shuffler.repository.GameRepository;
import ru.acuma.shuffler.service.api.GameService;

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
    private TGame buildGame(TEvent event) {
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

        return TGame.builder()
            .redTeam(redTeam)
            .blueTeam(blueTeam)
            .index(event.getTgGames().size() + 1)
            .startedAt(LocalDateTime.now())
            .state(GameState.ACTIVE)
            .build();
    }

    private TGame save(TGame tgGame, TEvent event) {
//        var mappedGame = gameMapper.toGame(tgGame).setEvent(event);
//        tgGame.setId(gameRepository.save(mappedGame).getId());
        teamService.save(tgGame.getBlueTeam(), tgGame);
        teamService.save(tgGame.getRedTeam(), tgGame);

        return tgGame;
    }

    @Override
    public void nextGame(TEvent event) {
        var game = buildGame(event);
        save(game, event);
        event.applyGame(game);
    }

    @Override
    public void handleGameCheck(TEvent event) {
        var game = event.getCurrentGame();
        switch (game.getState()) {
            case RED_CHECKING -> {
                game.getRedTeam().setIsWinner(true);
                finishGameWithWinner(game);
            }
            case BLUE_CHECKING -> {
                game.getBlueTeam().setIsWinner(true);
                finishGameWithWinner(game);
            }
            case CANCEL_CHECKING -> finishCancelledGame(game);

        }
        saveGameData(event);
    }

    private void finishGameWithWinner(TGame game) {
        game.setState(GameState.FINISHED)
            .setFinishedAt(LocalDateTime.now());
//        teamService.fillLastGameMate(game.getWinnerTeam());
//        teamService.fillLastGameMate(game.getLoserTeam());
    }

    private void finishCancelledGame(TGame game) {
        game.setState(GameState.CANCELLED)
            .setFinishedAt(LocalDateTime.now());
    }

    private void saveGameData(TEvent event) {
        var game = event.getCurrentGame();
        switch (game.getState()) {
            case CANCELLED:
                break;
            case FINISHED:
                ratingService.update(event);
                game.getPlayers().forEach(TEventPlayer::increaseGameCount);
//                teamService(game.getWinnerTeam());
        }
        gameRepository.save(gameMapper.toGame(game));
    }

}