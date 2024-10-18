package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    private final VS plugin;

    public PlayerDropItemListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerDropItem(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
        if(this.plugin.getPlayerManager().getBuildPlayers().contains(player) || playerData.getDuelArena() != null) {
            if (playerData.getDuelArena() != null) {
                if(event.getItemDrop().getItemStack().getType() == Material.BOWL) {
                    event.getItemDrop().remove();
                } else {
                    playerData.getDuelArena().getDroppedItems().add(event.getItemDrop());
                }
            }
            return;
        }
        event.setCancelled(true);
    }
}
