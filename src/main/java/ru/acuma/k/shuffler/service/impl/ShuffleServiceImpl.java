package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.service.ShuffleService;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.acuma.k.shuffler.model.enums.Values.GAME_PLAYERS_COUNT;

@Service
@RequiredArgsConstructor
public class ShuffleServiceImpl implements ShuffleService {

    @Override
    public List<KickerEventPlayer> shuffle(KickerEvent event) {

        List<KickerEventPlayer> members = event.getPlayers().values()
                .stream()
                .filter(member -> !member.isLeft())
                .collect(Collectors.toList());

        if (members.size() < GAME_PLAYERS_COUNT) {
            return null;
        }

        int minGames = members.stream()
                .min(Comparator.comparingInt(KickerEventPlayer::getGameCount))
                .orElse(new KickerEventPlayer().setGameCount(0))
                .getGameCount();
        int maxGames = members.stream()
                .max(Comparator.comparingInt(KickerEventPlayer::getGameCount))
                .orElse(new KickerEventPlayer().setGameCount(0))
                .getGameCount();

        if (minGames == maxGames) {
            return shuffleEvenly(members);
        }
        List<KickerEventPlayer> players = new ArrayList<>();
        for (int i = minGames; i <= maxGames; i++) {
            int vacancy = GAME_PLAYERS_COUNT - players.size();
            var shuffled = shufflePriority(members, i);

            if (shuffled.size() >= vacancy) {
                players.addAll(shuffled.subList(0, vacancy));
            } else {
                players.addAll(shuffled);
            }
            if (players.size() == 4) {
                break;
            }
        }
        return players;
    }

    @SneakyThrows
    private List<KickerEventPlayer> shuffleEvenly(List<KickerEventPlayer> members) {
        Collections.shuffle(members, SecureRandom.getInstance("SHA1PRNG", "SUN"));
        return members.subList(0, GAME_PLAYERS_COUNT);
    }

    @SneakyThrows
    private List<KickerEventPlayer> shufflePriority(List<KickerEventPlayer> members, int index) {
        List<KickerEventPlayer> priority = members.stream()
                .filter(member -> member.getGameCount() == index)
                .collect(Collectors.toList());
        if (priority.size() > GAME_PLAYERS_COUNT) {
            return shuffleEvenly(priority);
        }
        Collections.shuffle(members, SecureRandom.getInstance("SHA1PRNG", "SUN"));
        return priority;
    }
}
