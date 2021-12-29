package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.domain.Player;
import ru.acuma.k.shuffler.model.domain.Team;

import java.util.List;

public interface TeamService {

    Team teamBuilding(List<Player> players);


}
