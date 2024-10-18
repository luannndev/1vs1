package dev.luan.vs.listener;

import dev.luan.vs.VS;
import dev.luan.vs.kits.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.List;

public class InventoryClickListener implements Listener {

    private final VS plugin;
    private final List<Material> blockedMaterialDatas;

    public InventoryClickListener(final VS plugin) {
        this.plugin = plugin;
        this.blockedMaterialDatas = Arrays.asList(Material.WHITE_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS, Material.BROWN_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE, Material.GLASS_PANE);
    }

    @EventHandler
    public void inventoryClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if(event.getView().getTitle().contains("Stats")) {
            event.setCancelled(true);
        }
        if(this.plugin.getPlayerManager().getPlayerDatas().get(player).getDuelArena() != null) return;
        if(!this.plugin.getPlayerManager().getBuildPlayers().contains(player)) {
            event.setCancelled(true);
        }

        if(event.getClickedInventory() == null || event.getView().getTitle() == null) return;
        if(event.getCurrentItem() == null || this.blockedMaterialDatas.contains(event.getCurrentItem().getType())) return;
        if(event.getClickedInventory().equals(player.getInventory())) return;

        if(event.getView().getTitle().contains("Kit wählen | ")) {
            event.setCancelled(true);
            final String player_name = ChatColor.stripColor(event.getView().getTitle().split("Kit wählen | ")[2]);
            final KitManager.VSKit vsKit = this.plugin.getKitManager().getVSKitByMaterial(event.getCurrentItem().getType());
            if(vsKit == null) return;
            if(player_name.contains("1vs1-Warteschlange")) {
                this.plugin.getQueueManager().setQueuePlayer(player, vsKit);
                return;
            }
            player.performCommand("duel " + player_name + " " + vsKit.getKey());
            event.getView().close();
            return;
        }

        if(event.getView().getTitle().contains("Laufende Spiele")) {
            event.setCancelled(true);
            if(event.getCurrentItem().getType() == Material.BARRIER) return;
            final String arenaid = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            player.performCommand("spectate " + arenaid.split(" - ")[1]);
            return;
        }
    }
}
