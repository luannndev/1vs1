package dev.luan.vs.database.top;

import dev.luan.vs.VS;
import dev.luan.vs.database.gamerank.GameRankManager;
import dev.luan.vs.database.stats.StatsManager;
import dev.luan.vs.messages.ServerMessage;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TopManager {

    private final VS plugin;
    private final HashMap<Integer, ArmorStand> topHologramDatas;
    private Location topHologramLocation;

    public TopManager(final VS plugin) {
        this.plugin = plugin;

        this.topHologramLocation = plugin.getLocationManager().getLocation("top_hologram");
        this.topHologramDatas = new HashMap<>();
        if(this.topHologramLocation == null) return;
        this.topHologramLocation = this.topHologramLocation.add(0, 2.5, 0);
        final List<String> lines = new ArrayList<>();
        lines.add(ServerMessage.getString("1vs1", ServerMessage.MessageColor.PASTEL_PURPLE));
        lines.add("§7Season §8» §f" + this.plugin.getStatsManager().getCurrentSeason());
        lines.add("§7§m-----------------------------");
        for(int rank = 1; rank <= 10; rank++) {
            lines.add(" ");
        }

        int current = 1;
        for(final String line : lines) {
            final ArmorStand armorStand = (ArmorStand) this.topHologramLocation.getWorld().spawnEntity(this.topHologramLocation.add(0, -0.25, 0), EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(line);
            armorStand.setInvulnerable(true);
            armorStand.setSmall(true);
            this.topHologramDatas.put(current, armorStand);
            ++current;
        }
        this.updateHologram();
    }

    public void updateHologram() {
        int rank = 1;
        for(int i = 4; i <= 13; i++) {
            String player_name = "§cKein Spieler";
            int statValue = -1;
            GameRankManager.GameRank gameRank = GameRankManager.GameRank.UNRATED;
            if(plugin.getStatsManager().getTopPlayers(StatsManager.StatPeriod.SEASONAL, "wins").containsKey(rank)) {
                final UUID uuid = plugin.getStatsManager().getTopPlayers(StatsManager.StatPeriod.SEASONAL, "wins").get(rank);
                player_name = plugin.getStatsManager().getPlayerName(uuid);
                statValue = plugin.getStatsManager().getStat(uuid, StatsManager.StatPeriod.SEASONAL, "wins");
                gameRank = plugin.getGameRankManager().getPlayersGameRankType(uuid, StatsManager.StatPeriod.SEASONAL, "wins");
            }
            String rankColor = "§7";
            if(rank == 1) rankColor = "§6§l";
            else if(rank == 2) rankColor = "§7§l";
            else if(rank == 3) rankColor = "§c§l";
            this.topHologramDatas.get(i).setCustomName(rankColor + "#" + rank + " §7" + player_name + (statValue == -1 ? "" : " §8[" + gameRank.getShortName() + "§8] §8| §f" + statValue + " §7Wins"));
            ++rank;
        }
    }

    public void removeTopHologram() {
        for(final ArmorStand topHologram : this.topHologramDatas.values()) {
            topHologram.remove();
        }
    }
}
