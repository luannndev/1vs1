package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.arena.ArenaManager;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    private final VS plugin;

    public EntityDamageByEntityListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void entityDamageByEntity(final EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof ArmorStand) {
            final Player damagerPlayer = (Player) event.getDamager();
            for(final ArmorStand armorStand : this.plugin.getQueueManager().getEntityDatas().values()) {
                if(!event.getEntity().equals(armorStand)) continue;
                damagerPlayer.openInventory(this.plugin.getKitManager().getInventory("1vs1-Warteschlange"));
            }
            return;
        }
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);

            final Player damagerPlayer = (Player) event.getDamager();
            final PlayerManager.PlayerData damagerPlayerData = this.plugin.getPlayerManager().getPlayerDatas().get(damagerPlayer);
            if(playerData.getDuelArena() == null) {
                event.setCancelled(true);
                if(damagerPlayer.getItemInHand().getType() == Material.SHEARS) {
                    damagerPlayer.performCommand("duel " + player.getName());
                    return;
                }
                return;
            }

            if(!playerData.getDuelArena().equals(damagerPlayerData.getDuelArena())) {
                event.setCancelled(true);
                return;
            }
            if(playerData.getDuelArena().getArenaState() != ArenaManager.ArenaState.INGAME) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
