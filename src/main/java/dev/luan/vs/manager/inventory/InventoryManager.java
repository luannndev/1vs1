package dev.luan.vs.manager.inventory;

import dev.luan.vs.VS;
import dev.luan.vs.arena.ArenaManager;
import dev.luan.vs.database.stats.StatsManager;
import dev.luan.vs.messages.ServerMessage;
import dev.luan.vs.utilities.ItemHelper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class InventoryManager {

    private final VS plugin;
    private final HashMap<PublicInventory, Inventory> inventoryDatas;

    public InventoryManager(final VS plugin) {
        this.plugin = plugin;
        this.inventoryDatas = new HashMap<>();
        for(final PublicInventory publicInventory : PublicInventory.values()) {
            this.inventoryDatas.put(publicInventory, publicInventory.getInventory());
            this.buildInventory(publicInventory);
        }
    }

    public Inventory getInventory(final PublicInventory publicInventory) {
        return this.inventoryDatas.get(publicInventory);
    }

    private void buildInventory(final PublicInventory publicInventory) {
        final Inventory inventory = this.inventoryDatas.get(publicInventory);

        if(publicInventory == PublicInventory.LEADERBOARDS) {
            for(int i = 0; i < 45; i++) {
                inventory.setItem(i, ItemHelper.getItem(Material.GRAY_STAINED_GLASS_PANE, "§7", 1));
            }
            this.updateInventory(publicInventory);
            return;
        }

        if(publicInventory == PublicInventory.RUNNING_GAMES) {
            for(int i = 0; i < 54; i++) {
                if(i >= 9 && i <= 44) continue;
                inventory.setItem(i, ItemHelper.getItem(Material.GRAY_STAINED_GLASS_PANE, "§7", 1));
            }
            this.updateInventory(publicInventory);
            return;
        }
    }

    int rank = 1;
    public void updateInventory(final PublicInventory publicInventory) {
        final Inventory inventory = this.inventoryDatas.get(publicInventory);
        if(publicInventory == PublicInventory.LEADERBOARDS) {
            inventory.setItem(12, InventoryItems.GOLD_TROPHY.getItemStack());
            inventory.setItem(13, InventoryItems.SILVER_TROPHY.getItemStack());
            inventory.setItem(14, InventoryItems.BRONZE_TROPHY.getItemStack());
            for(int i = 21; i < 24; i++) {
                inventory.setItem(i, this.getTopPlayer(rank));
                ++rank;
            }
            for(int i = 28; i < 35; i++) {
                inventory.setItem(i, this.getTopPlayer(rank));
                ++rank;
            }
            rank = 1;
            return;
        }

        if(publicInventory == PublicInventory.RUNNING_GAMES) {
            for(int i = 9; i < 45; i++) {
                inventory.setItem(i, new ItemStack(Material.AIR));
            }
            int current = 9;
            for(final ArenaManager.DuelArena duelArena : this.plugin.getArenaManager().getDuelArenas()) {
                if(duelArena.getArenaState() == ArenaManager.ArenaState.LOBBY) continue;
                final ItemStack itemStack = duelArena.getMapTemplate().getItemStack().clone();
                final ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ServerMessage.getString(duelArena.getMapTemplate().getDisplayName(), ServerMessage.MessageColor.PASTEL_PURPLE) + " §8- §7" + duelArena.getArenaId());
                itemMeta.setLore(Arrays.asList(
                        " ",
                        "§c" + duelArena.getPoints().get(duelArena.getPlayers().get(0)) + " §8- §7" + duelArena.getPlayers().get(0).getName(),
                        "§8VS",
                        "§c" + duelArena.getPoints().get(duelArena.getPlayers().get(1)) + " §8- §7" + duelArena.getPlayers().get(1).getName()));
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(current, itemStack);
                ++current;
            }
            if(current == 9) {
                inventory.setItem(22, ItemHelper.getItem(Material.BARRIER, "§cKeine laufenden Runden!", 1));
            }
            return;
        }
    }

    private ItemStack getTopPlayer(final int rank) {
        ItemStack itemStack = ItemHelper.getSkullByURL("§7#" + rank + " §cKein Spieler", "d34e063cafb467a5c8de43ec78619399f369f4a52434da8017a983cdd92516a0");
        if(this.plugin.getStatsManager().getTopPlayers(StatsManager.StatPeriod.SEASONAL, "wins").containsKey(rank)) {
            final UUID uuid = this.plugin.getStatsManager().getTopPlayers(StatsManager.StatPeriod.SEASONAL, "wins").get(rank);
            final int wins = this.plugin.getStatsManager().getStat(uuid, StatsManager.StatPeriod.SEASONAL, "wins");
            if(wins <= 0) return itemStack;
            final String player_name = this.plugin.getStatsManager().getPlayerName(uuid);
            itemStack = ItemHelper.getSkull("§7#" + rank + " §7" + player_name, uuid, Arrays.asList(
                    " ",
                    "§7Ranking  §8» " + this.plugin.getGameRankManager().getPlayersGameRankType(uuid, StatsManager.StatPeriod.SEASONAL, "wins").getName() + " §8(§7Platz #" + this.plugin.getStatsManager().getTopRank(uuid, StatsManager.StatPeriod.SEASONAL, "wins") + "§8)",
                    "§7Kills §8» §f" + this.plugin.getStatsManager().getStat(uuid, StatsManager.StatPeriod.SEASONAL, "kills"),
                    "§7Tode §8» §f" + this.plugin.getStatsManager().getStat(uuid, StatsManager.StatPeriod.SEASONAL, "deaths"),
                    "§7K/Dr §8» §f" + this.plugin.getStatsManager().getPlayerKDR(uuid, StatsManager.StatPeriod.SEASONAL),
                    "§7Matches §8» §f" + this.plugin.getStatsManager().getStat(uuid, StatsManager.StatPeriod.SEASONAL, "matches"),
                    "§7Gewonnen §8» §f" + wins));
        }
        return itemStack;
    }

    @Getter
    public enum PublicInventory {

        LEADERBOARDS(Bukkit.createInventory(null, 9*5, "Leaderboards")),
        RUNNING_GAMES(Bukkit.createInventory(null, 9*6, "Laufende Spiele"));

        final Inventory inventory;
        PublicInventory(final Inventory inventory) {
            this.inventory = inventory;
        }
    }

    @Getter
    public enum InventoryItems {

        GOLD_TROPHY(ItemHelper.getSkullByURL("§6Platz #1", "5d308ae27b58b69645497f9da86597eda947eacd10c29e3d4bbf3bc76ceb1eab")),
        SILVER_TROPHY(ItemHelper.getSkullByURL("§7Platz #2", "536a06b15c55e1ddb6d399b0386a525bec001e5d98cbfa941b8fba7b40622370")),
        BRONZE_TROPHY(ItemHelper.getSkullByURL("§cPlatz #3", "566a5b7bdfaa93d44c50c644cd045c398a5010a79779007290c9a78cf898623a"));

        final ItemStack itemStack;
        InventoryItems(final ItemStack itemStack) {
            this.itemStack = itemStack;
        }
    }
}
