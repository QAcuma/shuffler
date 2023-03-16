package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.dto.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TeamPlayer;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class TeamPlayerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "team", ignore = true)
    public abstract TeamPlayer toTeamPlayer(TgEventPlayer player, Long teamId);

}
