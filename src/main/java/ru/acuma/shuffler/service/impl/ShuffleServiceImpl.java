package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.service.ShuffleService;

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
    public List<TgEventPlayer> shuffle(TgEvent event) {

        List<TgEventPlayer> members = event.getActivePlayers();

        if (members.size() < Values.GAME_PLAYERS_COUNT) {
            throw new NoSuchElementException("Not enough players to start event");
        }

        int minGames = members.stream()
                .min(Comparator.comparingInt(TgEventPlayer::getGameCount))
                .orElseThrow(() -> new NoSuchElementException("Group member not found"))
                .getGameCount();
        int maxGames = members.stream()
                .max(Comparator.comparingInt(TgEventPlayer::getGameCount))
                .orElseThrow(() -> new NoSuchElementException("Group member not found"))
                .getGameCount();

        if (minGames == maxGames) {
            return shuffleEvenly(members);
        }
        List<TgEventPlayer> players = new ArrayList<>();
        for (int i = minGames; i <= maxGames; i++) {
            int vacancy = Values.GAME_PLAYERS_COUNT - players.size();
            List<TgEventPlayer> shuffled = shufflePriority(members, i);

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
    private List<TgEventPlayer> shuffleEvenly(List<TgEventPlayer> members) {
        Collections.shuffle(members, SecureRandom.getInstance("SHA1PRNG", "SUN"));
        if (members.size() < Values.GAME_PLAYERS_COUNT) {
            return members;
        }
        return members.subList(0, Values.GAME_PLAYERS_COUNT);
    }

    @SneakyThrows
    private List<TgEventPlayer> shufflePriority(List<TgEventPlayer> members, int index) {
        List<TgEventPlayer> priority = members.stream()
                .filter(member -> member.getGameCount() == index)
                .collect(Collectors.toList());
        return shuffleEvenly(priority);
    }
}
