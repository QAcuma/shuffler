package ru.acuma.shuffler.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.entity.Event;
import ru.acuma.shuffler.model.entity.Game;

import java.util.stream.Stream;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class GameMapper {

    @Autowired
    protected TeamMapper teamMapper;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "event", source = "event")
    @Mapping(target = "status", source = "game.status")
    public abstract Game toGame(TGame game, Event event);

    @AfterMapping
    protected void mapPlayers(@MappingTarget Game game, TGame eventGame, Event event) {
        var teams = Stream.of(eventGame.getBlueTeam(), eventGame.getRedTeam())
            .map(teamMapper::toTeam)
            .toList();
        game.withTeams(teams);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "finishedAt", source = "finishedAt")
    @Transactional(propagation = Propagation.MANDATORY)
    public abstract void updateGame(@MappingTarget Game game, TGame finishedGame);
}
