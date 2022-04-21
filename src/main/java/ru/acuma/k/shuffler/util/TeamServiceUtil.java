package ru.acuma.k.shuffler.util;

import lombok.SneakyThrows;
import ru.acuma.k.shuffler.model.entity.KickerTeam;

public final class TeamServiceUtil {

    @SneakyThrows
    public static boolean checkTeamMatches(KickerTeam team) {
        if (team.getPlayer1().getLastGamePlayer() == null || team.getPlayer2().getLastGamePlayer() == null) {
            return false;
        }
        return team.getPlayer1().getLastGamePlayer().equals(team.getPlayer2())
                || team.getPlayer2().getLastGamePlayer().equals(team.getPlayer1());
    }

}
