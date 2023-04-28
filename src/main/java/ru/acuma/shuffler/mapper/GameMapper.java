package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.entity.Game;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class GameMapper {

    @Mapping(target = "event", ignore = true)
    public abstract Game toGame(TGame source);

}
