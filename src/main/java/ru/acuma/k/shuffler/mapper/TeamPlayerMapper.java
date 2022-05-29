package ru.acuma.k.shuffler.mapper;

import org.springframework.stereotype.Component;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.tables.pojos.TeamPlayer;

@Component
public class TeamPlayerMapper {

    public TeamPlayer toTeamPlayer(KickerEventPlayer player, Long teamId) {
        return new TeamPlayer()
                .setPlayerId(player.getId())
                .setTeamId(teamId);
    }

}
