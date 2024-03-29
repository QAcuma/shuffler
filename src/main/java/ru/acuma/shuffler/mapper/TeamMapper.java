package ru.acuma.shuffler.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TTeam;
import ru.acuma.shuffler.model.entity.Team;

@Mapper(
    config = MapperConfiguration.class,
    uses = {
        GameMapper.class,
        PlayerMapper.class
    }
)
public abstract class TeamMapper {

    @Autowired
    protected PlayerMapper playerMapper;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "player1", source = "player1")
    @Mapping(target = "player2", source = "player2")
    @Mapping(target = "score", expression = "java(mapScore(player1, player2))")
    @Mapping(target = "isWinner", constant = "false")
    public abstract TTeam createTeam(final TEventPlayer player1, final TEventPlayer player2);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "isWinner", source = "team.isWinner")
    public abstract Team mapTeam(TTeam team);

    protected Integer mapScore(final TEventPlayer player1, final TEventPlayer player2) {
        return (player1.getRatingContext().getScore() + player2.getRatingContext().getScore()) / 2;
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "isWinner", source = "isWinner")
    @Transactional(propagation = Propagation.MANDATORY)
    public abstract void update(@MappingTarget Team savedWinner, TTeam winnerTeam);

}
