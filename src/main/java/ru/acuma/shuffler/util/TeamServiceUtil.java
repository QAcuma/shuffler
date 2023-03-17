package ru.acuma.shuffler.util;

import ru.acuma.shuffler.model.domain.TgTeam;

public final class TeamServiceUtil {

    public static boolean checkTeamMatches(TgTeam team) {
        if (team.getPlayer1().getLastGamePlayer() == null || team.getPlayer2().getLastGamePlayer() == null) {
            return false;
        }
        return team.getPlayer1().getLastGamePlayer().equals(team.getPlayer2())
                || team.getPlayer2().getLastGamePlayer().equals(team.getPlayer1());
    }

}
