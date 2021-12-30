package ru.acuma.k.shuffler.service.impl;

import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.service.TeamService;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    @Override
    public KickerTeam teamBuilding(List<KickerEventPlayer> players) {

        return new KickerTeam(players.get(0), players.get(1));
    }
}
