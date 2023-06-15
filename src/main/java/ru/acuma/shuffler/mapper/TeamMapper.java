package ru.acuma.shuffler.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TTeam;
import ru.acuma.shuffler.model.entity.Team;
import ru.acuma.shuffler.model.entity.TeamPlayer;

import java.util.List;

@Mapper(
    config = MapperConfiguration.class,
    uses = GameMapper.class
)
public abstract class TeamMapper {

    public TTeam createTeam(final TEventPlayer player1, final TEventPlayer player2) {
        var score = (player1.getRatingContext().getScore() + player2.getRatingContext().getScore()) / 2;

        return TTeam.builder()
            .player1(player1)
            .player2(player2)
            .score(score)
            .isWinner(Boolean.FALSE)
            .build();
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "isWinner", source = "team.isWinner")
    public abstract Team toTeam(TTeam team);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "player", source = "player")
    public abstract TeamPlayer toTeamPlayer(TEventPlayer player);

    @AfterMapping
    protected void fillTeamPlayers(@MappingTarget Team team, TTeam eventTeam) {
        var players = mapTeamPlayers(eventTeam);
        team.withPlayers(players);
    }

    protected List<TeamPlayer> mapTeamPlayers(TTeam team) {
        return team.getPlayers().stream()
            .map(this::toTeamPlayer)
            .toList();
    }
}
