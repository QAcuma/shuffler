package ru.acuma.shuffler.service;

import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TgTeam;

import java.util.List;

public interface TeamService {

    TgTeam buildTeam(List<TgEventPlayer> player);

    TgTeam save(TgTeam team, Long gameId);

    default void fillLastGameMate(TgTeam team) {
        team.getPlayer1().setLastGamePlayer(team.getPlayer2());
        team.getPlayer2().setLastGamePlayer(team.getPlayer1());
    }

    TgTeam update(TgTeam team);

    void saveTeamPlayer(TgEventPlayer player, Long teamId);
}
