package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.dto.TgTeam;
import ru.acuma.shuffler.tables.pojos.Team;

@Mapper(componentModel = "spring")
public abstract class TeamMapper {

    @Mapping(source = "winner", target = "isWinner")
    public abstract Team toTeam(TgTeam team);

    @Mapping(source = "team.winner", target = "isWinner")
    public abstract Team toTeam(TgTeam team, Long gameId);

}
