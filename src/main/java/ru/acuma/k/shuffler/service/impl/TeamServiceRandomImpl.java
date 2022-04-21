package ru.acuma.k.shuffler.service.impl;

import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.service.TeamService;
import ru.acuma.k.shuffler.util.TeamServiceUtil;

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
public class TeamServiceRandomImpl implements TeamService {

    @Override
    public KickerTeam teamBuilding(List<KickerEventPlayer> players) {
        if (players.size() < GAME_PLAYERS_COUNT / 2) {
            throw new IllegalArgumentException("Not enough players");
        }

        return build(players, 20);
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
