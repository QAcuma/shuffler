package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.TeamMapper;
import ru.acuma.shuffler.mapper.TeamPlayerMapper;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.domain.TTeam;
import ru.acuma.shuffler.repository.TeamPlayerRepository;
import ru.acuma.shuffler.repository.TeamRepository;
import ru.acuma.shuffler.util.TeamServiceUtil;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

import static ru.acuma.shuffler.model.constant.Constants.GAME_PLAYERS_COUNT;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamMapper teamMapper;
    private final TeamPlayerMapper teamPlayerMapper;
    private final TeamRepository teamRepository;
    private final TeamPlayerRepository teamPlayerRepository;

    public TTeam buildTeam(List<TEventPlayer> players) {
        if (players.size() < GAME_PLAYERS_COUNT / 2) {
            throw new IllegalArgumentException("Not enough players");
        }

        return build(players, 20);
    }

    public TTeam save(TTeam team, TGame game) {
        var mappedTeam = teamMapper.toTeam(team, game);
        team.getPlayers().forEach(player -> saveTeamPlayer(player, team.getId()));

        return team;
    }

    public TTeam update(TTeam team) {
        var mappedTeam = teamMapper.toTeam(team);

        return team;
    }

    public void saveTeamPlayer(TEventPlayer player, Long teamId) {
        var mappedTeamPlayer = teamPlayerMapper.toTeamPlayer(player, teamId);
        teamPlayerRepository.save(mappedTeamPlayer);
    }

    @SneakyThrows
    private TTeam build(List<TEventPlayer> players, int retries) {
        var team = TTeam.builder()
            .player1(players.get(0))
            .player2(players.get(1))
            .build();
        if (retries == 0) {
            return team;
        }

        if (TeamServiceUtil.checkTeamMatches(team)) {
            Collections.shuffle(players, SecureRandom.getInstance("SHA1PRNG", "SUN"));
            return build(players, --retries);
        }

        return team;
    }
}
