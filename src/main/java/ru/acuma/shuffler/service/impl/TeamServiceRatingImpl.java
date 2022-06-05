package ru.acuma.shuffler.service.impl;

import com.google.common.collect.Iterables;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.mapper.TeamMapper;
import ru.acuma.shuffler.mapper.TeamPlayerMapper;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TgTeam;
import ru.acuma.shuffler.service.TeamService;
import ru.acuma.shuffler.util.TeamServiceUtil;
import ru.acuma.shufflerlib.repository.TeamPlayerRepository;
import ru.acuma.shufflerlib.repository.TeamRepository;

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
    private final TeamRepository teamRepository;
    private final TeamPlayerRepository teamPlayerRepository;

    @Value("${rating.spread-distance}")
    private long spreadDistance;

    @SneakyThrows
    @Override
    public TgTeam teamBuilding(List<TgEventPlayer> players) {
        List<TgEventPlayer> playersCopy = new ArrayList<>(players);
        playersCopy.forEach(
                player -> player.setSpreadRating(player.getRating()
                        + ThreadLocalRandom.current().nextLong(-spreadDistance, spreadDistance))
        );
        playersCopy.sort(Comparator.comparingLong(TgEventPlayer::getSpreadRating).reversed());

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
    public TgTeam save(TgTeam team, Long gameId) {
        var mappedTeam = teamMapper.toTeam(team, gameId);
        team.setId(teamRepository.save(mappedTeam));
        team.getPlayers().forEach(player -> saveTeamPlayer(player, team.getId()));

        return team;
    }

    @Override
    public TgTeam update(TgTeam team) {
        var mappedTeam = teamMapper.toTeam(team);
        team.setId(teamRepository.save(mappedTeam));

        return team;
    }

    @Override
    public void saveTeamPlayer(TgEventPlayer player, Long teamId) {
        var mappedTeamPlayer = teamPlayerMapper.toTeamPlayer(player, teamId);
        teamPlayerRepository.save(mappedTeamPlayer);
    }

    @SneakyThrows
    private TgTeam calculateTeam(List<TgEventPlayer> players) {
        var player1 = players.stream()
                .findFirst()
                .orElseThrow(() -> new InstanceNotFoundException("Player not found"));
        var player2 = Iterables.getLast(players);

        if (player1.getLastGamePlayer() == null || player2.getLastGamePlayer() == null) {
            return new TgTeam(player1, player2);
        }
        if ((player1.getLastGamePlayer().equals(player2) || player2.getLastGamePlayer().equals(player1)) && players.size() >= 2) {
            players.remove(player2);
            return calculateTeam(players);
        } else if (players.size() < 2) {
            throw new IllegalArgumentException("Can't shuffle unique team");
        }
        return new TgTeam(player1, player2);
    }

}
