package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.domain.TTeam;
import ru.acuma.shuffler.model.entity.Team;

@Mapper(
    config = MapperConfiguration.class,
    uses = GameMapper.class
)
public abstract class TeamMapper {

    @Mapping(target = "game", ignore = true)
    public abstract Team toTeam(TTeam team);

    @Mapping(target = "isWinner", source = "team.isWinner")
    @Mapping(target = "id", source = "team.id")
    public abstract Team toTeam(TTeam team, TGame game);

}
