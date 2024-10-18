package dev.luan.vs.database.stats;

import dev.luan.vs.VS;
import dev.luan.vs.messages.ServerMessage;
import dev.luan.vs.utilities.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class StatsMenu {

    private final VS plugin;
    private final HashMap<StatsManager.StatPeriod, ItemStack> statPeriodItemDatas;

    public StatsMenu(final VS plugin) {
        this.plugin = plugin;
        this.statPeriodItemDatas = new HashMap<>();
        for(final StatsManager.StatPeriod statPeriod : StatsManager.StatPeriod.values()) {
            this.statPeriodItemDatas.put(statPeriod, new ItemBuilder(Material.PLAYER_HEAD).setName(statPeriod.getDisplayName()).setSkullOwner(statPeriod.getTextureData()).build());
        }
    }

    public Inventory getStatMenu(final UUID uuid) {
        final String player_name = this.plugin.getStatsManager().getPlayerName(uuid);
        final Inventory inventory = Bukkit.createInventory(null, 9*3, "Stats von " + player_name);

        inventory.setItem(4, new ItemBuilder(Material.PLAYER_HEAD).setName("§7" + player_name).setSkullOwner(uuid).build());

        int slot = 9;
        for(final StatsManager.StatPeriod statPeriod : StatsManager.StatPeriod.values()) {
            inventory.setItem(slot,
                    new ItemBuilder(this.statPeriodItemDatas.get(statPeriod))
                            .setName(ServerMessage.getString(statPeriod.getDisplayName(), ServerMessage.MessageColor.PASTEL_PURPLE))
                            .setLore((this.plugin.getStatsManager().getStat(uuid, statPeriod, "matches") <= 0 ?
                                    Arrays.asList(
                                            " ",
                                            "§cDu hast kein 1vs1",
                                            "§cin diesem Zeitraum gespielt!"
                                    )
                                    :
                                    Arrays.asList(
                                            " ",
                                            "§7Ranking §8» " + this.plugin.getGameRankManager().getPlayersGameRankType(uuid, statPeriod, "wins").getName() + " §8(§7Platz #" + this.plugin.getStatsManager().getTopRank(uuid, statPeriod, "wins") + "§8)",
                                            "§7Kills §8» " + ServerMessage.getString(String.valueOf(this.plugin.getStatsManager().getStat(uuid, statPeriod, "kills")), ServerMessage.MessageColor.REGULAR_WHITE),
                                            "§7Tode §8» " + ServerMessage.getString(String.valueOf(this.plugin.getStatsManager().getStat(uuid, statPeriod, "deaths")), ServerMessage.MessageColor.REGULAR_WHITE),
                                            "§7K/Dr §8» " + ServerMessage.getString(this.plugin.getStatsManager().getPlayerKDR(uuid, statPeriod), ServerMessage.MessageColor.REGULAR_WHITE),
                                            "§7Matches §8» " + ServerMessage.getString(String.valueOf(this.plugin.getStatsManager().getStat(uuid, statPeriod, "matches")), ServerMessage.MessageColor.REGULAR_WHITE),
                                            "§7Gewonnen §8» " + ServerMessage.getString(String.valueOf(this.plugin.getStatsManager().getStat(uuid, statPeriod, "wins")), ServerMessage.MessageColor.REGULAR_WHITE)
                                    )))
                            .build());

            slot += 2;
        }

        return inventory;
    }
}
