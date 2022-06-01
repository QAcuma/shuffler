package ru.acuma.shuffler.service;

import ru.acuma.shuffler.model.entity.GameEventPlayer;
import ru.acuma.shuffler.model.entity.GameTeam;

import java.util.List;

public interface TeamService {

    GameTeam teamBuilding(List<GameEventPlayer> player);

    GameTeam save(GameTeam team, Long gameId);

    default void fillLastGameMate(GameTeam team) {
        team.getPlayer1().setLastGamePlayer(team.getPlayer2());
        team.getPlayer2().setLastGamePlayer(team.getPlayer1());
    }

    void saveTeamPlayer(GameEventPlayer player, Long teamId);
}
