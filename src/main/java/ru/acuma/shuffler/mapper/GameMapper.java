package ru.acuma.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.Game;

@Component
public class GameMapper extends BaseMapper {

    public ru.acuma.k.shuffler.tables.pojos.Game toGame(Game game) {
        mapperFactory.classMap(Game.class, ru.acuma.k.shuffler.tables.pojos.Game.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(game, ru.acuma.k.shuffler.tables.pojos.Game.class);
    }

}
