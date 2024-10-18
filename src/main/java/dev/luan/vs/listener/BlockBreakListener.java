package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final VS plugin;

    public BlockBreakListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void blockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
        if(this.plugin.getPlayerManager().getBuildPlayers().contains(player) || playerData.getDuelArena() != null) {
            if(playerData.getDuelArena() != null) {
                playerData.getDuelArena().getPlacedBlocks().remove(event.getBlock());
            }
            return;
        }
        event.setCancelled(true);
    }
}
