package net.rodald.captureHorse.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Teams {

    public static Team getEntityTeam(Player player) {
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Team team : sb.getTeams()) {
            if (team.hasEntity(player)) {
                return team;
            }
        }
        return null;
    }
}
