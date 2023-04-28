package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.entity.TeamPlayer;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class TeamPlayerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "team", ignore = true)
    public abstract TeamPlayer toTeamPlayer(TEventPlayer player, Long teamId);

}
