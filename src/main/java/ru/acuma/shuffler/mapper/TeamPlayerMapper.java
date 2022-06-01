package ru.acuma.shuffler.mapper;

import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.GameEventPlayer;
import ru.acuma.k.shuffler.tables.pojos.TeamPlayer;

@Component
public class TeamPlayerMapper {

    public TeamPlayer toTeamPlayer(GameEventPlayer player, Long teamId) {
        return new TeamPlayer()
                .setPlayerId(player.getId())
                .setTeamId(teamId);
    }

}
