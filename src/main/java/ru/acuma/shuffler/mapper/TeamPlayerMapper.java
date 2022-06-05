package ru.acuma.shuffler.mapper;

import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.tables.pojos.TeamPlayer;

@Component
public class TeamPlayerMapper {

    public TeamPlayer toTeamPlayer(TgEventPlayer player, Long teamId) {
        return new TeamPlayer()
                .setPlayerId(player.getId())
                .setTeamId(teamId);
    }

}
