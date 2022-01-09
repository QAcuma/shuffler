package ru.acuma.k.shuffler.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.service.TeamService;

import javax.management.InstanceNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TeamServiceImpl implements TeamService {

    @Value("${rating.spread-distance}")
    private long spreadDistance;

    @SneakyThrows
    @Override
    public KickerTeam teamBuilding(List<KickerEventPlayer> players) {

        players.forEach(player -> player.setSpreadRating(player.getRating() + ThreadLocalRandom.current().nextLong(-spreadDistance, spreadDistance)));

        KickerEventPlayer player1 = players.stream()
                .min(Comparator.comparingLong(KickerEventPlayer::getSpreadRating))
                .orElseThrow(() -> new InstanceNotFoundException("Player not found"));
        players.remove(player1);

        KickerEventPlayer player2 = players.stream()
                .max(Comparator.comparingLong(KickerEventPlayer::getSpreadRating))
                .orElseThrow(() -> new InstanceNotFoundException("Player not found"));
        players.remove(player2);

        return new KickerTeam(player1, player2);
    }

}
