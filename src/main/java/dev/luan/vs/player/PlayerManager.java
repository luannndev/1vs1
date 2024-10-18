package dev.luan.vs.player;

import dev.luan.vs.VS;
import dev.luan.vs.arena.ArenaManager;
import dev.luan.vs.database.stats.StatsManager;
import dev.luan.vs.kits.KitManager;
import dev.luan.vs.messages.ServerMessage;
import dev.luan.vs.scoreboard.ScoreboardAPI;
import dev.luan.vs.utilities.ItemHelper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class PlayerManager {

    private static VS plugin;
    private final HashMap<Player, ArenaManager.DuelArena> spectatorPlayers;
    private final HashMap<Player, PlayerData> playerDatas;
    private final List<Player> buildPlayers;
    private final List<String> mapPoolDatas;

    public PlayerManager(final VS vs) {
        plugin = vs;
        this.spectatorPlayers = new HashMap<>();
        this.playerDatas = new HashMap<>();
        this.buildPlayers = new ArrayList<>();
        this.mapPoolDatas = new ArrayList<>();
    }

    public void setSpectator(final Player player, final ArenaManager.DuelArena duelArena) {
        for(final Player players : duelArena.getPlayers()) {
            players.hidePlayer(player);
        }
        this.resetPlayer(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, -1, 1, true, false));
        player.getInventory().setItem(8, PlayerManager.VSItems.FIRE_CHARGE.getItemStack());
        player.teleport(duelArena.getCenter());
        player.setAllowFlight(true);
        this.spectatorPlayers.put(player, duelArena);
        this.plugin.getQueueManager().leaveQueue(player);
    }

    public void removeSpectator(final Player player) {
        for(final Player players : Bukkit.getOnlinePlayers()) {
            players.showPlayer(player);
        }
        this.spectatorPlayers.remove(player);
        this.plugin.getLocationManager().teleportPlayer(player, "spawn");
        this.playerDatas.get(player).setLobbyItems();
    }

    public void resetPlayer(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.setAllowFlight(false);
        player.getActivePotionEffects().forEach(potionEffect -> {
            player.removePotionEffect(potionEffect.getType());
        });
    }

    @Getter
    public static class PlayerData {

        private Player player;
        private final ScoreboardAPI scoreboardAPI;
        private final List<Player> receivedRequests;
        private ArenaManager.DuelArena duelArena;
        @Setter
        private KitManager.VSKit VSKit;

        public PlayerData(final Player player) {
            this.player = player;
            this.receivedRequests = new ArrayList<>();
            this.scoreboardAPI = new ScoreboardAPI(this.player);
            this.scoreboardAPI.setDisplayName(" " + ServerMessage.getString("1vs1", ServerMessage.MessageColor.PASTEL_PURPLE) + " ");
            this.setScoreboard();
        }

        public void playerQuit() {
            this.revokeSendedRequests();

            plugin.getPlayerManager().getPlayerDatas().remove(this.player);
            if(plugin.getPlayerManager().getSpectatorPlayers().containsKey(this.player)) plugin.getPlayerManager().getSpectatorPlayers().remove(this.player);
            plugin.getQueueManager().leaveQueue(player);
        }

        public void revokeSendedRequests() {
            for(final PlayerData playerData : plugin.getPlayerManager().getPlayerDatas().values()) {
                if(playerData.getDuelArena() != null) continue;
                if(playerData.getReceivedRequests().contains(this.player)) playerData.getReceivedRequests().remove(this.player);
                playerData.setScoreboard();
            }
        }

        public void setScoreboard() {
            if(this.duelArena != null) return;
            this.scoreboardAPI.updateLines(
                    " ",
                    "§8»»»»»",
                    (this.receivedRequests.size() <= 0 ? "§cKeine Anfrage" : "§7" + this.receivedRequests.get(this.receivedRequests.size() - 1).getName() + " "),
                    " ",
                    "§7Season §8»",
                    " §f" + plugin.getStatsManager().getCurrentSeason() + " ",
                    " ",
                    "§7Ranking §8» " + plugin.getGameRankManager().getPlayersGameRankType(this.player.getUniqueId(), StatsManager.StatPeriod.SEASONAL, "wins").getName() + " §8(§7#" + plugin.getStatsManager().getTopRank(this.player.getUniqueId(), StatsManager.StatPeriod.SEASONAL, "wins") + "§8)",
                    "§7Matches §8» §f" + plugin.getStatsManager().getStat(this.player.getUniqueId(), StatsManager.StatPeriod.SEASONAL, "matches"),
                    "§7Gewonnen §8» §f" + plugin.getStatsManager().getStat(this.player.getUniqueId(), StatsManager.StatPeriod.SEASONAL, "wins") + " §7 ",
                    "§7K/Dr §8» §f" + plugin.getStatsManager().getPlayerKDR(this.player.getUniqueId(), StatsManager.StatPeriod.SEASONAL),
                    " ");
        }

        private String title = "1vs1";
        private int i = 0;
        private boolean forward = true;
        public void setScoreboardDisplayName() {
            String final_title = this.title.substring(0, i);
            this.scoreboardAPI.setDisplayName(" " + ServerMessage.getString(final_title + (i < title.length() && i != 0 ? "_" : ""), ServerMessage.MessageColor.PASTEL_PURPLE) + " ");
            if(i < title.length() && this.forward) {
                if(forward) i++;
            } else {
                this.forward = false;
                if(i <= 0) {
                    this.forward = true;
                    this.i = 0;
                    return;
                }
                i--;
            }
        }

        public void setLobbyItems() {
            plugin.getPlayerManager().resetPlayer(this.player);
            this.player.getInventory().setItem(1, VSItems.SHEARS.getItemStack());
            this.player.getInventory().setItem(2, VSItems.GLOBE_BANNER_PATTERN.getItemStack());
            this.player.getInventory().setItem(4, VSItems.PLAYER_HEAD.getItemStack());
            this.player.getInventory().setItem(5, VSItems.ENDER_EYE.getItemStack());
            this.player.getInventory().setItem(7, VSItems.FIRE_CHARGE.getItemStack());
        }

        public void setDuelArena(final ArenaManager.DuelArena duelArena) {
            this.duelArena = duelArena;
        }
    }

    @Getter
    public enum VSItems {

        SHEARS(ItemHelper.getItem(Material.SHEARS, "§7Spieler " + ServerMessage.getString("herausfordern", ServerMessage.MessageColor.PASTEL_PURPLE), 1)),
        GLOBE_BANNER_PATTERN(ItemHelper.getItem(Material.GLOBE_BANNER_PATTERN, ServerMessage.getString("Maps", ServerMessage.MessageColor.PASTEL_PURPLE) + " §7einstellen", 1)),
        PLAYER_HEAD(ItemHelper.getSkullByURL(ServerMessage.getString("Leaderboards", ServerMessage.MessageColor.PASTEL_PURPLE), "e34a592a79397a8df3997c43091694fc2fb76c883a76cce89f0227e5c9f1dfe")),
        ENDER_EYE(ItemHelper.getItem(Material.ENDER_EYE, "§7Laufende Spiele", 1)),
        FIRE_CHARGE(ItemHelper.getItem(Material.FIRE_CHARGE, "§7Zur §cLobby", 1));

        final ItemStack itemStack;
        VSItems(final ItemStack itemStack) {
            this.itemStack = itemStack;
        }
    }
}
