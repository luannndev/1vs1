package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final VS plugin;

    public PlayerJoinListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(final PlayerJoinEvent event) {
        event.setJoinMessage("");
        final Player player = event.getPlayer();
        if(!player.hasPlayedBefore() || player.hasPlayedBefore()) {
            if(!this.plugin.getStatsManager().isInList(player.getUniqueId())) this.plugin.getStatsManager().insertPlayer(player.getUniqueId(), player.getName());
            this.plugin.getLocationManager().teleportPlayer(player, "spawn");
        }
        final PlayerManager.PlayerData playerData = new PlayerManager.PlayerData(player);
        this.plugin.getPlayerManager().getPlayerDatas().put(player, playerData);
        playerData.setLobbyItems();
        Bukkit.getOnlinePlayers().forEach(players -> {
            players.showPlayer(player);
            player.showPlayer(players);
        });
    }
}
