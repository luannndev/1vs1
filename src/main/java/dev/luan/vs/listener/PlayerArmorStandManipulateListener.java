package dev.luan.vs.listener;

import dev.luan.vs.VS;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerArmorStandManipulateListener implements Listener {

    private final VS plugin;

    public PlayerArmorStandManipulateListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerArmorStandManipulate(final PlayerInteractAtEntityEvent event) {
        final Player player = event.getPlayer();
        if(this.plugin.getPlayerManager().getBuildPlayers().contains(player)) return;
        event.setCancelled(true);
        if(!(event.getRightClicked() instanceof ArmorStand)) return;
        for(final ArmorStand armorStand : this.plugin.getQueueManager().getEntityDatas().values()) {
            if(!event.getRightClicked().equals(armorStand)) continue;
            this.plugin.getQueueManager().leaveQueue(player);
        }
    }
}
