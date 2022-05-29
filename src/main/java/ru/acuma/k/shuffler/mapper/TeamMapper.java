package ru.acuma.k.shuffler.mapper;

import org.springframework.stereotype.Component;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.tables.pojos.Team;

import java.util.ArrayList;
import java.util.List;

@Component
public class TeamMapper extends BaseMapper {

    public List<Team> toTeams(KickerGame game) {
        List<Team> teams = new ArrayList<>();
        mapperFactory.classMap(KickerTeam.class, Team.class)
                .byDefault()
                .register();
        teams.add(toTeam(game.getBlueTeam(), game.getId()).setId(game.getId()));
        teams.add(toTeam(game.getRedTeam(), game.getId()).setId(game.getId()));

        return teams;
    }

    public Team toTeam(KickerTeam team, Long gameID) {
        return new Team()
                .setGameId(gameID)
                .setIsWinner(team.isWinner());
    }

}
