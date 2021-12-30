package ru.acuma.k.shuffler.service.impl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.service.TeamService;

import java.util.Comparator;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @SneakyThrows
    @Override
    public KickerTeam teamBuilding(List<KickerEventPlayer> players) {

        KickerEventPlayer player1 = players.stream().min(Comparator.comparingInt(KickerEventPlayer::getRating)).get();
        players.remove(player1);

        KickerEventPlayer player2 = players.stream().max(Comparator.comparingInt(KickerEventPlayer::getRating)).get();
        players.remove(player2);

        return new KickerTeam(player1, player2);
    }

}
