package ru.acuma.shuffler.service.game;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.TeamMapper;
import ru.acuma.shuffler.mapper.TeamPlayerMapper;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TgTeam;
import ru.acuma.shuffler.service.api.TeamService;
import ru.acuma.shuffler.util.TeamServiceUtil;
import ru.acuma.shufflerlib.repository.TeamPlayerRepository;
import ru.acuma.shufflerlib.repository.TeamRepository;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

import static ru.acuma.shuffler.model.enums.Values.GAME_PLAYERS_COUNT;

@Service
@ConditionalOnProperty(
        prefix = "rating",
        name = "team-build-strategy",
        havingValue = "random"
)
@RequiredArgsConstructor
public class TeamServiceRandomImpl implements TeamService {

    private final TeamMapper teamMapper;
    private final TeamPlayerMapper teamPlayerMapper;
    private final TeamRepository teamRepository;
    private final TeamPlayerRepository teamPlayerRepository;

    @Override
    public TgTeam buildTeam(List<TgEventPlayer> players) {
        if (players.size() < GAME_PLAYERS_COUNT / 2) {
            throw new IllegalArgumentException("Not enough players");
        }

        return build(players, 20);
    }

    @Override
    public TgTeam save(TgTeam team, Long gameId) {
        var mappedTeam = teamMapper.toTeam(team, gameId);
        team.setId(teamRepository.save(mappedTeam));
        team.getPlayers().forEach(player -> saveTeamPlayer(player, team.getId()));

        return team;
    }

    @Override
    public TgTeam update(TgTeam team) {
        var mappedTeam = teamMapper.toTeam(team);
        team.setId(teamRepository.update(mappedTeam));

        return team;
    }

    @Override
    public void saveTeamPlayer(TgEventPlayer player, Long teamId) {
        var mappedTeamPlayer = teamPlayerMapper.toTeamPlayer(player, teamId);
        teamPlayerRepository.save(mappedTeamPlayer);
    }

    @SneakyThrows
    private TgTeam build(List<TgEventPlayer> players, int retries) {
        var team = new TgTeam(players.get(0), players.get(1));
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
