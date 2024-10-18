package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final VS plugin;

    public PlayerDeathListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerDeath(final PlayerDeathEvent event) {
        event.setDeathMessage("");
        event.getDrops().clear();
        event.setDroppedExp(0);
        event.getEntity().spigot().respawn();
        final Player player = event.getEntity();
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
            if(playerData.getDuelArena() != null) {
                playerData.getDuelArena().playerDeath(player);
            }
        }, 2L);
    }
}
