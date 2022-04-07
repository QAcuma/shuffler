package ru.acuma.k.shuffler.util;

import com.google.common.collect.Iterables;
import lombok.SneakyThrows;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerTeam;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

public final class TeamServiceUtil {

    @SneakyThrows
    public static boolean checkSecondTeam(List<KickerEventPlayer> players, KickerTeam team) {
        List<KickerEventPlayer> playersCopy = new ArrayList<>(players);
        playersCopy.removeAll(team.getPlayers());

        var player1 = playersCopy.stream()
                .findFirst()
                .orElseThrow(() -> new InstanceNotFoundException("Player not found"));
        var player2 = Iterables.getLast(playersCopy);

        if (player1.getLastGamePlayer() == null || player2.getLastGamePlayer() == null) {
            return true;
        }
        if (player1.getLastGamePlayer().equals(player2) || player2.getLastGamePlayer().equals(player1)) {
            return false;
        }
        return true;
    }

}
