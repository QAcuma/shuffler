package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;

import java.util.List;

public interface TeamService {

    KickerTeam teamBuilding(List<KickerPlayer> kickerPlayers);


}
