package ru.acuma.k.shuffler.service.impl;

import com.google.common.collect.Iterables;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
public class TeamServiceRatingImpl implements TeamService {

    private final TeamMapper teamMapper;
    private final TeamPlayerMapper teamPlayerMapper;
    private final TeamDao teamDao;
    private final TeamPlayerDao teamPlayerDao;

    @Value("${rating.spread-distance}")
    private long spreadDistance;

    @SneakyThrows
    @Override
    public KickerTeam teamBuilding(List<KickerEventPlayer> players) {
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

        if (TeamServiceUtil.checkTeamMatches(team)) {
            return teamBuilding(players);
        }

        return team;
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
