package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.entity.GameEventPlayer;
import ru.acuma.shuffler.service.ShuffleService;
import ru.acuma.shuffler.model.enums.Values;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShuffleServiceImpl implements ShuffleService {

    @SneakyThrows
    @Override
    public List<GameEventPlayer> shuffle(GameEvent event) {

        List<GameEventPlayer> members = event.getActivePlayers();

        if (members.size() < Values.GAME_PLAYERS_COUNT) {
            throw new NoSuchElementException("Not enough players to start event");
        }

        int minGames = members.stream()
                .min(Comparator.comparingInt(GameEventPlayer::getGameCount))
                .orElseThrow(() -> new NoSuchElementException("Group member not found"))
                .getGameCount();
        int maxGames = members.stream()
                .max(Comparator.comparingInt(GameEventPlayer::getGameCount))
                .orElseThrow(() -> new NoSuchElementException("Group member not found"))
                .getGameCount();

        if (minGames == maxGames) {
            return shuffleEvenly(members);
        }
        List<GameEventPlayer> players = new ArrayList<>();
        for (int i = minGames; i <= maxGames; i++) {
            int vacancy = Values.GAME_PLAYERS_COUNT - players.size();
            List<GameEventPlayer> shuffled = shufflePriority(members, i);

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
    private List<GameEventPlayer> shuffleEvenly(List<GameEventPlayer> members) {
        Collections.shuffle(members, SecureRandom.getInstance("SHA1PRNG", "SUN"));
        if (members.size() < Values.GAME_PLAYERS_COUNT) {
            return members;
        }
        return members.subList(0, Values.GAME_PLAYERS_COUNT);
    }

    @SneakyThrows
    private List<GameEventPlayer> shufflePriority(List<GameEventPlayer> members, int index) {
        List<GameEventPlayer> priority = members.stream()
                .filter(member -> member.getGameCount() == index)
                .collect(Collectors.toList());
        return shuffleEvenly(priority);
    }
}
