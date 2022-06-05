package ru.acuma.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.TgTeam;
import ru.acuma.shuffler.tables.pojos.Team;
import ru.acuma.shufflerlib.mapper.BaseMapper;

@Component
public class TeamMapper extends BaseMapper {

    public Team toTeam(TgTeam team) {
        return toTeam(team, null);
    }

    public Team toTeam(TgTeam source, Long gameId) {
        mapperFactory.classMap(TgTeam.class, Team.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        Team target = mapper.map(source, Team.class);
        return target.setGameId(gameId)
                .setIsWinner(source.isWinner());
    }

}
