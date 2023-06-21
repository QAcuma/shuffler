package ru.acuma.shuffler.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.entity.Event;
import ru.acuma.shuffler.model.entity.Game;

@Mapper(
    config = MapperConfiguration.class
)
public interface GameMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "event", source = "event")
    @Mapping(target = "status", source = "game.status")
    @Transactional(propagation = Propagation.MANDATORY)
    Game toGame(TGame game, Event event);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "finishedAt", source = "finishedAt")
    @Transactional(propagation = Propagation.MANDATORY)
    void updateGame(@MappingTarget Game game, TGame finishedGame);
}
