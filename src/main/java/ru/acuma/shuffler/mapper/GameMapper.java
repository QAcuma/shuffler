package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import ru.acuma.shuffler.model.dto.TgGame;
import ru.acuma.shuffler.tables.pojos.Game;

@Mapper(uses = OffsetDateTimeMapper.class, componentModel = "spring")
public abstract class GameMapper {

    public abstract Game toGame(TgGame source);

}
