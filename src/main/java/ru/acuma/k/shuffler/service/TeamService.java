package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;

import java.util.List;

public interface TeamService {

    KickerTeam teamBuilding(List<KickerEventPlayer> player);

    KickerTeam save(KickerTeam team, Long gameId);

    default void fillLastGameMate(KickerTeam team) {
        team.getPlayer1().setLastGamePlayer(team.getPlayer2());
        team.getPlayer2().setLastGamePlayer(team.getPlayer1());
    }

    void saveTeamPlayer(KickerEventPlayer player, Long teamId);
}
