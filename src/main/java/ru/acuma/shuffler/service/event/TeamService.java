package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.TeamMapper;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TTeam;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Team;
import ru.acuma.shuffler.model.entity.TeamPlayer;
import ru.acuma.shuffler.util.TeamServiceUtil;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.acuma.shuffler.model.constant.Constants.GAME_PLAYERS_COUNT;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamMapper teamMapper;

    public TTeam buildTeam(List<TEventPlayer> players) {
        if (players.size() < GAME_PLAYERS_COUNT / 2) {
            throw new IllegalArgumentException("Not enough players");
        }

        return build(players, 20);
    }

    @SneakyThrows
    private TTeam build(List<TEventPlayer> players, int retries) {
        var team = teamMapper.createTeam(players.get(0), players.get(1));
        if (retries == 0) {
            return team;
        }

        if (TeamServiceUtil.checkTeamMatches(team)) {
            Collections.shuffle(players, SecureRandom.getInstance("SHA1PRNG", "SUN"));
            return build(players, --retries);
        }

        return team;
    }

    public void fillLastGameMate(final TTeam team) {
        team.getPlayer1().setLastGamePlayer(team.getPlayer2());
        team.getPlayer2().setLastGamePlayer(team.getPlayer1());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Team getTeamBySide(final List<Team> teams, final List<TEventPlayer> players) {
        return teams.stream()
            .filter(team -> hasAnyPlayer(team, players))
            .findFirst()
            .orElseThrow(() -> new DataException(ExceptionCause.MISSING_WINNER_TEAM));
    }

    private boolean hasAnyPlayer(final Team team, final List<TEventPlayer> players) {
        var playersId = players.stream()
            .map(TEventPlayer::getId)
            .toList();

        return team.getTeamPlayers().stream()
            .map(TeamPlayer::getPlayer)
            .map(Player::getId)
            .anyMatch(playersId::contains);
    }

    public void updateTeam(final List<Team> teams, final TTeam winnerTeam) {
        Optional.ofNullable(winnerTeam)
            .flatMap(winner -> teams.stream()
                .filter(savedTeam -> savedTeam.getId().equals(winner.getId()))
                .findFirst()
            )
            .ifPresent(savedWinner -> teamMapper.update(savedWinner, winnerTeam));
    }

    public Team mapTeam(TTeam team, final List<TeamPlayer> teamPlayers) {
        return teamMapper.mapTeam(team)
            .withPlayers(teamPlayers);
    }
}
