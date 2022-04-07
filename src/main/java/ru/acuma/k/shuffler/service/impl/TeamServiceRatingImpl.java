package ru.acuma.k.shuffler.service.impl;

import com.google.common.collect.Iterables;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.service.TeamService;
import ru.acuma.k.shuffler.util.TeamServiceUtil;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@ConditionalOnProperty(
        prefix = "rating",
        name = "team-build-strategy",
        havingValue = "rating"
)
public class TeamServiceRatingImpl implements TeamService {

    @Value("${rating.spread-distance}")
    private long spreadDistance;

    @SneakyThrows
    @Override
    public KickerTeam teamBuilding(List<KickerEventPlayer> players, long spread) {
        if (spread > spreadDistance * 4) {
            throw new IllegalArgumentException("Can't shuffle unique team");
        }
        List<KickerEventPlayer> playersCopy = new ArrayList<>(players);
        playersCopy.forEach(
                player -> player.setSpreadRating(player.getRating()
                        + ThreadLocalRandom.current().nextLong(-spreadDistance, spreadDistance))
        );
        playersCopy.sort(Comparator.comparingLong(KickerEventPlayer::getSpreadRating).reversed());

        if (playersCopy.size() == 2) {
            return calculateTeam(playersCopy);
        }

        var team = calculateTeam(playersCopy);

        if (!TeamServiceUtil.checkSecondTeam(players, team)) {
            return teamBuilding(players, spread + spreadDistance);
        }

        return team;
    }

    @SneakyThrows
    private KickerTeam calculateTeam(List<KickerEventPlayer> players) {
        var player1 = players.stream()
                .findFirst()
                .orElseThrow(() -> new InstanceNotFoundException("Player not found"));
        var player2 = Iterables.getLast(players);

        if (player1.getLastGamePlayer() == null || player2.getLastGamePlayer() == null) {
            return new KickerTeam(player1, player2);
        }
        if ((player1.getLastGamePlayer().equals(player2) || player2.getLastGamePlayer().equals(player1)) && players.size() >= 2) {
            players.remove(player2);
            return calculateTeam(players);
        } else if (players.size() < 2) {
            throw new IllegalArgumentException("Can't shuffle unique team");
        }
        return new KickerTeam(player1, player2);
    }

}
