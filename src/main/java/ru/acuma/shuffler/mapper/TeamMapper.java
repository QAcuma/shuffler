package ru.acuma.shuffler.mapper;

import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.Game;
import ru.acuma.shuffler.model.entity.GameTeam;
import ru.acuma.k.shuffler.tables.pojos.Team;

import java.util.ArrayList;
import java.util.List;

@Component
public class TeamMapper extends BaseMapper {

    public List<Team> toTeams(Game game) {
        List<Team> teams = new ArrayList<>();
        mapperFactory.classMap(GameTeam.class, Team.class)
                .byDefault()
                .register();
        teams.add(toTeam(game.getBlueTeam(), game.getId()).setId(game.getId()));
        teams.add(toTeam(game.getRedTeam(), game.getId()).setId(game.getId()));

        return teams;
    }

    public Team toTeam(GameTeam team, Long gameID) {
        return new Team()
                .setGameId(gameID)
                .setIsWinner(team.isWinner());
    }

}
