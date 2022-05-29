package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.mapper.TeamMapper;
import ru.acuma.k.shuffler.mapper.TeamPlayerMapper;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.service.TeamService;
import ru.acuma.k.shuffler.util.TeamServiceUtil;
import ru.acuma.shufflerlib.dao.TeamDao;
import ru.acuma.shufflerlib.dao.TeamPlayerDao;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

import static ru.acuma.k.shuffler.model.enums.Values.GAME_PLAYERS_COUNT;

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
    public KickerTeam teamBuilding(List<KickerEventPlayer> players) {
        if (players.size() < GAME_PLAYERS_COUNT / 2) {
            throw new IllegalArgumentException("Not enough players");
        }

        return build(players, 20);
    }

    @Override
    public KickerTeam save(KickerTeam team, Long gameId) {
        var mappedTeam = teamMapper.toTeam(team, gameId);
        team.setId(teamDao.save(mappedTeam));
        team.getPlayers().forEach(player -> saveTeamPlayer(player, team.getId()));

        return team;
    }

    @Override
    public void saveTeamPlayer(KickerEventPlayer player, Long teamId) {
        var mappedTeamPlayer = teamPlayerMapper.toTeamPlayer(player, teamId);
        teamPlayerDao.save(mappedTeamPlayer);
    }

    @SneakyThrows
    private KickerTeam build(List<KickerEventPlayer> players, int retries) {
        if (retries == 0) {
            throw new IllegalArgumentException("Can't shuffle unique team");
        }
        var team = new KickerTeam(players.get(0), players.get(1));

        if (TeamServiceUtil.checkTeamMatches(team)) {
            Collections.shuffle(players, SecureRandom.getInstance("SHA1PRNG", "SUN"));
            return build(players, --retries);
        }

        return team;
    }
}
