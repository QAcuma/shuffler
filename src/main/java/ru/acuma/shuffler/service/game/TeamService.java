package ru.acuma.shuffler.service.game;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.TeamMapper;
import ru.acuma.shuffler.mapper.TeamPlayerMapper;
import ru.acuma.shuffler.model.domain.TgEventPlayer;
import ru.acuma.shuffler.model.domain.TgGame;
import ru.acuma.shuffler.model.domain.TgTeam;
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

    public TgTeam buildTeam(List<TgEventPlayer> players) {
        if (players.size() < GAME_PLAYERS_COUNT / 2) {
            throw new IllegalArgumentException("Not enough players");
        }

        return build(players, 20);
    }

    public TgTeam save(TgTeam team, TgGame game) {
        var mappedTeam = teamMapper.toTeam(team, game);
        team.getPlayers().forEach(player -> saveTeamPlayer(player, team.getId()));

        return team;
    }

    public TgTeam update(TgTeam team) {
        var mappedTeam = teamMapper.toTeam(team);

        return team;
    }

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
