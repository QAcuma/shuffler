package ru.acuma.k.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Component;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.tables.pojos.Game;

@Component
public class GameMapper extends BaseMapper {

    public Game toGame(KickerGame kickerGame) {
        mapperFactory.classMap(KickerGame.class, Game.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(kickerGame, Game.class);
    }

}
