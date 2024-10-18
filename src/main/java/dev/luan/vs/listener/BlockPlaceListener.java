package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    private final VS plugin;

    public BlockPlaceListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void blockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
        if(this.plugin.getPlayerManager().getBuildPlayers().contains(player) || playerData.getDuelArena() != null) {
            if(playerData.getDuelArena() != null) {
                if(event.getBlockPlaced().getType().equals(Material.TNT)) {
                    event.getBlockPlaced().setType(Material.AIR);
                    final TNTPrimed tntPrimed = (TNTPrimed) event.getBlockPlaced().getWorld().spawnEntity(event.getBlockPlaced().getLocation(), EntityType.PRIMED_TNT);
                    tntPrimed.setFuseTicks(25);
                }
                playerData.getDuelArena().getPlacedBlocks().add(event.getBlockPlaced());
            }
            return;
        }
        event.setCancelled(true);
    }
}
