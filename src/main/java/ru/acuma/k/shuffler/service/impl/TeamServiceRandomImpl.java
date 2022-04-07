package ru.acuma.k.shuffler.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.service.TeamService;
import ru.acuma.k.shuffler.util.TeamServiceUtil;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

@Service
@ConditionalOnProperty(
        prefix = "rating",
        name = "team-build-strategy",
        havingValue = "random"
)
public class TeamServiceRandomImpl implements TeamService {

    @Value("${rating.spread-distance}")
    private long spreadDistance;

    @SneakyThrows
    @Override
    public KickerTeam teamBuilding(List<KickerEventPlayer> players, long limiter) {
        if (limiter == spreadDistance) {
            limiter = 0;
        }
        if (players.size() < 2 || limiter > 10) {
            throw new IllegalArgumentException("Can't shuffle unique team");
        }
        var team = new KickerTeam(players.get(0), players.get(1));

        if (!TeamServiceUtil.checkSecondTeam(players, team)) {
            Collections.shuffle(players, SecureRandom.getInstance("SHA1PRNG", "SUN"));
            return teamBuilding(players, ++limiter);
        }

        return team;
    }
}
