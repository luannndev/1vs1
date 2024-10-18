package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.arena.ArenaManager;
import dev.luan.vs.manager.inventory.InventoryManager;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final VS plugin;

    public PlayerInteractListener(final VS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void playerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
        if(playerData.getDuelArena() != null && playerData.getDuelArena().getArenaState() == ArenaManager.ArenaState.INGAME) {
            if(event.getItem() == null) return;
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if(event.getItem().getType().equals(Material.WATER_BUCKET) || event.getItem().getType().equals(Material.LAVA_BUCKET)) {
                        final Block block = event.getClickedBlock().getLocation().add(0, 1, 0).getBlock();
                        playerData.getDuelArena().getPlacedBlocks().add(block);
                    }
                }
                if(event.getItem().getType().equals(Material.MUSHROOM_STEW)) {
                    if(player.getHealth() >= player.getMaxHealth()) return;
                    player.getItemInHand().setType(Material.BOWL);
                    player.playSound(player.getLocation(), Sound.ENTITY_HORSE_EAT, 1, 2);
                    if((player.getHealth() + 2) >= 20) {
                        player.setHealth(player.getMaxHealth());
                        return;
                    }
                    player.setHealth(player.getHealth() + 2.0);
                }
            }

            return;
        }
        if(!this.plugin.getPlayerManager().getBuildPlayers().contains(player)) {
            event.setCancelled(true);
        }

        if(event.getItem() == null) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(this.plugin.getPlayerManager().getPlayerDatas().get(player).getDuelArena() != null) return;
        if(this.plugin.getPlayerManager().getSpectatorPlayers().containsKey(player)) {
            this.plugin.getPlayerManager().removeSpectator(player);
            return;
        }

        if(event.getItem().equals(PlayerManager.VSItems.PLAYER_HEAD.getItemStack())) {
            player.openInventory(this.plugin.getInventoryManager().getInventory(InventoryManager.PublicInventory.LEADERBOARDS));
            return;
        }

        if(event.getItem().equals(PlayerManager.VSItems.ENDER_EYE.getItemStack())) {
            player.openInventory(this.plugin.getInventoryManager().getInventory(InventoryManager.PublicInventory.RUNNING_GAMES));
            return;
        }

        if(event.getItem().equals(PlayerManager.VSItems.FIRE_CHARGE.getItemStack())) {
            player.kickPlayer("lobby");
            return;
        }

    }
}
