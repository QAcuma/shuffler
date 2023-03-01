package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.dto.TgEventPlayer;
import ru.acuma.shuffler.model.dto.TgTeam;

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
