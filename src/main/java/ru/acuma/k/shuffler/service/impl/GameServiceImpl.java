package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.service.GameService;
import ru.acuma.k.shuffler.service.PlayerService;
import ru.acuma.k.shuffler.service.ShuffleService;
import ru.acuma.k.shuffler.service.TeamService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final ShuffleService shuffleService;

    @Override
    public KickerGame buildGame(KickerEvent event) {
        List<KickerEventPlayer> players = shuffleService.shuffle(event);
        KickerTeam team1 = teamService.teamBuilding(players);


        return null;
    }
}
