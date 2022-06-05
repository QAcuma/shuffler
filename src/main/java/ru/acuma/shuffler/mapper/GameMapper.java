package ru.acuma.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.TgGame;
import ru.acuma.shuffler.tables.pojos.Game;

@Component
public class GameMapper extends BaseMapper {

    public Game toGame(TgGame tgGame) {
        mapperFactory.classMap(TgGame.class, Game.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(tgGame, Game.class);
    }

}
