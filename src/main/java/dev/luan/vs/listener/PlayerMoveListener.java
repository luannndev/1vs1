package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.arena.ArenaManager;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMoveListener implements Listener {

    private final VS plugin;

    public PlayerMoveListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
        if(playerData.getDuelArena() != null) {
            if(playerData.getDuelArena().getArenaState() == ArenaManager.ArenaState.STARTING) {
                if(event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
                    player.teleport(event.getFrom());
                }
                return;
            }
            if(playerData.getDuelArena().getArenaState() == ArenaManager.ArenaState.END) return;
            if(player.getLocation().getY() <= 0.0) {
                player.damage(20);
                return;
            }
            return;
        }
        if(this.plugin.getPlayerManager().getSpectatorPlayers().containsKey(player)) {
            final ArenaManager.DuelArena duelArena = this.plugin.getPlayerManager().getSpectatorPlayers().get(player);
            if(this.playerAtArenaBorder(player, duelArena.getCenter(), duelArena.getMapTemplate().getBorder())) {
                Vector lV = duelArena.getCenter().toVector();
                Vector pV = player.getLocation().toVector();
                Vector v = lV.clone().subtract(pV).normalize().multiply(0.5D).setY(0.25D);

                player.setVelocity(v);
                player.playEffect(player.getLocation(), Effect.SMOKE, 1);
            }
            return;
        }

        if(player.getLocation().getY() <= 0.0) {
            this.plugin.getLocationManager().teleportPlayer(player, "spawn");
        }
    }

    private boolean playerAtArenaBorder(final Player player, final Location location, final double borderRadius) {
        double radius = borderRadius;
        if (player.getLocation().getX() > location.getX() + radius | player.getLocation().getX() < location.getX() - radius | player.getLocation().getZ() > location.getZ() + radius | player.getLocation().getZ() < location.getZ() - radius) {
            return true;
        }
        return false;
    }
}
