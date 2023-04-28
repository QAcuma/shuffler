package ru.acuma.shuffler.service.game;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventContext;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.constant.Constants;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShuffleService {

    @SneakyThrows
    public List<TEventPlayer> shuffle(TEvent event) {

        List<TEventPlayer> members = event.getActivePlayers();

        if (members.size() < Constants.GAME_PLAYERS_COUNT) {
            throw new NoSuchElementException("Not enough players to start event");
        }

        int minGames = members.stream()
            .map(TEventPlayer::getEventContext)
            .min(Comparator.comparingInt(TEventContext::getGameCount))
            .orElseThrow(() -> new NoSuchElementException("Group member not found"))
            .getGameCount();
        int maxGames = members.stream()
            .map(TEventPlayer::getEventContext)
            .max(Comparator.comparingInt(TEventContext::getGameCount))
            .orElseThrow(() -> new NoSuchElementException("Group member not found"))
            .getGameCount();

        if (minGames == maxGames) {
            return shuffleEvenly(members);
        }
        List<TEventPlayer> players = new ArrayList<>();
        for (int i = minGames; i <= maxGames; i++) {
            int vacancy = Constants.GAME_PLAYERS_COUNT - players.size();
            List<TEventPlayer> shuffled = shufflePriority(members, i);

            if (shuffled.size() >= vacancy) {
                players.addAll(shuffled.subList(0, vacancy));
            } else {
                players.addAll(shuffled);
            }
            if (players.size() == Constants.GAME_PLAYERS_COUNT) {
                break;
            }
        }
        return players;
    }

    @SneakyThrows
    private List<TEventPlayer> shuffleEvenly(List<TEventPlayer> members) {
        Collections.shuffle(members, SecureRandom.getInstance("SHA1PRNG", "SUN"));
        if (members.size() < Constants.GAME_PLAYERS_COUNT) {
            return members;
        }

        return members.subList(0, Constants.GAME_PLAYERS_COUNT);
    }

    @SneakyThrows
    private List<TEventPlayer> shufflePriority(List<TEventPlayer> members, int index) {
        List<TEventPlayer> priority = members.stream()
            .filter(member -> member.getEventContext().getGameCount() == index)
            .collect(Collectors.toList());

        return shuffleEvenly(priority);
    }
}
