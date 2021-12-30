package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.model.entity.KickerTeam;
import ru.acuma.k.shuffler.model.enums.GameState;
import ru.acuma.k.shuffler.service.GameService;
import ru.acuma.k.shuffler.service.PlayerService;
import ru.acuma.k.shuffler.service.ShuffleService;
import ru.acuma.k.shuffler.service.TeamService;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final TeamService teamService;
    private final ShuffleService shuffleService;

    @SneakyThrows
    @Override
    public void buildGame(KickerEvent event) {
        List<KickerEventPlayer> players = shuffleService.shuffle(event);
        if (players == null) {
            throw new InstanceNotFoundException("Недостаточно игроков для начала матча");
        }
        KickerTeam redTeam = teamService.teamBuilding(players);
        KickerTeam blueTeam = teamService.teamBuilding(players);
        KickerGame game = KickerGame.builder()
                .redTeam(redTeam)
                .blueTeam(blueTeam)
                .index(event.getGames().size() + 1)
                .startedAt(LocalDateTime.now())
                .state(GameState.STARTED)
                .build();
        event.newGame(game);
    }
}
