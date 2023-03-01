package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.dto.TgEventPlayer;
import ru.acuma.shuffler.tables.pojos.TeamPlayer;

@Mapper(componentModel = "spring")
public abstract class TeamPlayerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "player.id", target = "playerId")
    public abstract TeamPlayer toTeamPlayer(TgEventPlayer player, Long teamId);

}
