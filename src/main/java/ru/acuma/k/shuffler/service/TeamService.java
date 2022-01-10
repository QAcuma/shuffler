package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;

import java.util.List;

public interface TeamService {

    KickerTeam teamBuilding(List<KickerEventPlayer> players, long spreadDistance);

    void fillLastGameMate(KickerTeam team);


}
