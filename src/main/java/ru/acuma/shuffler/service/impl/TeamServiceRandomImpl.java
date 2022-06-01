package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.TeamMapper;
import ru.acuma.shuffler.mapper.TeamPlayerMapper;
import ru.acuma.shuffler.model.entity.GameEventPlayer;
import ru.acuma.shuffler.model.entity.GameTeam;
import ru.acuma.shuffler.service.TeamService;
import ru.acuma.shuffler.util.TeamServiceUtil;
import ru.acuma.shufflerlib.dao.TeamDao;
import ru.acuma.shufflerlib.dao.TeamPlayerDao;

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
    private final TeamDao teamDao;
    private final TeamPlayerDao teamPlayerDao;

    @Override
    public GameTeam teamBuilding(List<GameEventPlayer> players) {
        if (players.size() < GAME_PLAYERS_COUNT / 2) {
            throw new IllegalArgumentException("Not enough players");
        }

        return build(players, 20);
    }

    @Override
    public GameTeam save(GameTeam team, Long gameId) {
        var mappedTeam = teamMapper.toTeam(team, gameId);
        team.setId(teamDao.save(mappedTeam));
        team.getPlayers().forEach(player -> saveTeamPlayer(player, team.getId()));

        return team;
    }

    @Override
    public void saveTeamPlayer(GameEventPlayer player, Long teamId) {
        var mappedTeamPlayer = teamPlayerMapper.toTeamPlayer(player, teamId);
        teamPlayerDao.save(mappedTeamPlayer);
    }

    @SneakyThrows
    private GameTeam build(List<GameEventPlayer> players, int retries) {
        if (retries == 0) {
            throw new IllegalArgumentException("Can't shuffle unique team");
        }
        var team = new GameTeam(players.get(0), players.get(1));

        if (TeamServiceUtil.checkTeamMatches(team)) {
            Collections.shuffle(players, SecureRandom.getInstance("SHA1PRNG", "SUN"));
            return build(players, --retries);
        }

        return team;
    }
}
