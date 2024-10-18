package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.arena.ArenaManager;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    private final VS plugin;

    public EntityDamageListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void entityDamage(final EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
            if(playerData.getDuelArena() != null) {
                if(playerData.getDuelArena().getArenaState() == ArenaManager.ArenaState.INGAME) return;
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
        }
    }
}
