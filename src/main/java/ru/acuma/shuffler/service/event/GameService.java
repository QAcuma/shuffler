package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.GameMapper;
import ru.acuma.shuffler.mapper.PlayerMapper;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.constant.GameStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.entity.Game;
import ru.acuma.shuffler.repository.GameRepository;
import ru.acuma.shuffler.repository.ReferenceService;
import ru.acuma.shuffler.util.TimeMachine;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final TeamService teamService;
    private final ShuffleService shuffleService;
    private final RatingService ratingService;
    private final PlayerService playerService;
    private final ReferenceService referenceService;
    private final EventService eventService;
    private final GameMapper gameMapper;
    private final GameRepository gameRepository;
    private final GameStatusService gameStatusService;

    @SneakyThrows
    private TGame buildGame(TEvent event) {
        var players = Optional
            .ofNullable(shuffleService.shuffle(event))
            .orElseThrow(() -> new InstanceNotFoundException("Not enough players to start"));
        var redTeam = Optional
            .ofNullable(teamService.buildTeam(players))
            .orElseThrow(() -> new IllegalArgumentException("Red team is null"));
        var blueTeamPlayers = players.stream()
            .filter(Predicate.not(redTeam.getPlayers()::contains))
            .collect(Collectors.toList());
        var blueTeam = teamService.buildTeam(blueTeamPlayers);

        ratingService.applyBet(redTeam, blueTeam);

        return TGame.builder()
            .redTeam(redTeam)
            .blueTeam(blueTeam)
            .index(event.getTgGames().size() + 1)
            .order(event.getFinishedGames().size() + 1)
            .startedAt(TimeMachine.localDateTimeNow())
            .status(GameStatus.ACTIVE)
            .build();
    }

    public void beginGame(TEvent event) {
        var game = buildGame(event);
        event.applyGame(game);
    }

    public void finishGame(final TGame game) {
        switch (game.getStatus()) {
            case RED_CHECKING -> {
                game.getRedTeam().setIsWinner(Boolean.TRUE);
                finishGameWithWinner(game);
                increasePlayersGameCount(game);
            }
            case BLUE_CHECKING -> {
                game.getBlueTeam().setIsWinner(Boolean.TRUE);
                finishGameWithWinner(game);
                increasePlayersGameCount(game);
            }
            case CANCEL_CHECKING, EVENT_CHECKING, EVICT_CHECKING -> cancelGame(game);
        }
    }

    public void cancelGame(final TGame game) {
        gameStatusService.cancelled(game);
    }

    private void increasePlayersGameCount(final TGame game) {
        game.getPlayers().forEach(TEventPlayer::increaseGameCount);
    }

    private void finishGameWithWinner(final TGame game) {
        gameStatusService.finished(game);
        game.getWinnerTeam().applyRating(game.getId());
        game.getLoserTeam().applyRating(game.getId());
        teamService.fillLastGameMate(game.getWinnerTeam());
        teamService.fillLastGameMate(game.getLoserTeam());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Game find(final Long id) {
        return gameRepository.findById(id)
            .orElseThrow(() -> new DataException(ExceptionCause.GAME_NOT_FOUND, id));
    }

    @Transactional
    public void save(final TGame game, Long eventId) {
        var event = eventService.getReference(eventId);
        var mappedGame = gameMapper.toGame(game, event);
        var redTeamPlayers = playerService.mapTeamPlayers(game.getRedTeam().getPlayers());
        var blueTeamPlayers = playerService.mapTeamPlayers(game.getBlueTeam().getPlayers());
        var redTeam = teamService.mapTeam(game.getRedTeam(), redTeamPlayers);
        var blueTeam = teamService.mapTeam(game.getBlueTeam(), blueTeamPlayers);

        mappedGame.withTeams(List.of(redTeam, blueTeam));
        gameRepository.save(mappedGame);

        game.setId(mappedGame.getId());
        game.getRedTeam().setId(redTeam.getId());
        game.getBlueTeam().setId(blueTeam.getId());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void update(final TGame game) {
        var savedGame = find(game.getId());
        teamService.updateTeam(savedGame.getTeams(), game.getWinnerTeam());
        gameMapper.updateGame(savedGame, game);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Game getReference(Long gameId) {
        return referenceService.getReference(Game.class, gameId);
    }
}
