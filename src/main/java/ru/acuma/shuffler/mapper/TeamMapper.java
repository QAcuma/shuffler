package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import ru.acuma.shuffler.model.entity.TgTeam;
import ru.acuma.shuffler.tables.pojos.Team;
import ru.acuma.shufflerlib.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public abstract class TeamMapper extends BaseMapper {

    public abstract Team toTeam(TgTeam team);

    public abstract Team toTeam(TgTeam source, Long gameId);

}
