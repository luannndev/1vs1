package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final VS plugin;

    public PlayerQuitListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerQuit(final PlayerQuitEvent event) {
        event.setQuitMessage("");
        final Player player = event.getPlayer();
        final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
        if(playerData.getDuelArena() != null) {
            playerData.getDuelArena().arenaLeave(player, true);
        }
        this.plugin.getPlayerManager().getPlayerDatas().get(player).playerQuit();
    }
}
