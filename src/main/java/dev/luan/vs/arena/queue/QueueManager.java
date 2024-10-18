package dev.luan.vs.arena.queue;

import dev.luan.vs.VS;
import dev.luan.vs.kits.KitManager;
import dev.luan.vs.messages.ServerMessage;
import dev.luan.vs.utilities.ItemHelper;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class QueueManager {

    private final VS plugin;
    private final HashMap<KitManager.VSKit, List<Player>> queuePlayerDatas;
    private final HashMap<String, ArmorStand> entityDatas;

    public QueueManager(final VS plugin) {
        this.plugin = plugin;
        this.queuePlayerDatas = new HashMap<>();
        this.entityDatas = new HashMap<>();
        this.spawnEntity();
        for(final KitManager.VSKit vsKit : this.plugin.getKitManager().getVSKitDatas().values()) {
            this.queuePlayerDatas.put(vsKit, new ArrayList<>());
        }
    }

    public void spawnEntity() {
        final Location location = this.plugin.getLocationManager().getLocation("queue");
        if(location == null) return;

        final ArmorStand queue_players = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.1, 0), EntityType.ARMOR_STAND);
        queue_players.setItemInHand(new ItemStack(Material.NETHERITE_SWORD));
        queue_players.setHelmet(ItemHelper.getSkullByURL("", "22841b6670a7fb2773ae291b2467c07c7e8e2c2e453ec3939ab67b2659aea2c2"));
        queue_players.setChestplate(ItemHelper.getItem(Material.LEATHER_CHESTPLATE, "", Color.FUCHSIA));
        queue_players.setGravity(false);
        queue_players.setArms(true);
        queue_players.setBasePlate(false);
        queue_players.setCollidable(false);
        queue_players.setCustomNameVisible(true);
        queue_players.setCustomName("§7Beitreten");
        this.entityDatas.put("queue_players", queue_players);

        final ArmorStand queue_name = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 0.275, 0), EntityType.ARMOR_STAND);
        queue_name.setGravity(false);
        queue_name.setBasePlate(false);
        queue_name.setCollidable(false);
        queue_name.setVisible(false);
        queue_name.setCustomNameVisible(true);
        queue_name.setCustomName(ServerMessage.getString("ᴡᴀʀᴛᴇsᴄʜʟᴀɴɢᴇ", ServerMessage.MessageColor.PASTEL_PURPLE));
        this.entityDatas.put("queue_name", queue_name);
    }

    public void leaveQueue(final Player player) {
        boolean inQueue = false;
        for(final KitManager.VSKit vsKit : this.queuePlayerDatas.keySet()) {
            final List<Player> players = this.queuePlayerDatas.getOrDefault(vsKit, new ArrayList<>());
            if(!players.contains(player)) continue;
            inQueue = true;
            players.remove(player);
        }
        if(inQueue) {
            player.sendTitle(ServerMessage.getString("×", ServerMessage.MessageColor.RED), "§7Warteschlange verlassen", 1, 20, 10);
        }
    }

    public void setCustomName(final String entity_name, final String custom_name) {
        //this.entityDatas.get(entity_name).setCustomName(custom_name);
    }

    int i = 0;
    public void getQueueTask() {
        this.setCustomName("queue_players", "§7" + this.queuePlayerDatas.size() + "§8/§72");
        for(final KitManager.VSKit vsKit : this.queuePlayerDatas.keySet()) {
            this.queuePlayerDatas.getOrDefault(vsKit, new ArrayList<>()).forEach(players -> {
                players.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§e⌚ §7Warte auf einen Gegner§8" + (i >= 3 ? "..." : (i >= 2 ? ".." : (i >= 1 ? "." : "")))));
            });
        }
        ++i;
        if(i > 3) {
            i = 0;
        }
    }

    final HashMap<Player, Long> cooldown = new HashMap<>();
    public void setQueuePlayer(final Player player, final KitManager.VSKit vsKit) {
        final List<Player> players = this.queuePlayerDatas.getOrDefault(vsKit, new ArrayList<>());
        if(this.cooldown.containsKey(player) && System.currentTimeMillis() < this.cooldown.get(player)) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 2);
            return;
        }

        if(players.contains(player)) {
            this.leaveQueue(player);
            player.openInventory(this.plugin.getKitManager().getInventory("1vs1-Warteschlange"));
            return;
        }

        if(players.size() >= 1) {
            this.plugin.getArenaManager().searchArena(player, players.get(0), vsKit);
            return;
        }

        this.cooldown.put(player, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1));
        players.add(player);
        player.sendTitle(ServerMessage.getString("✔", ServerMessage.MessageColor.LIGHT_GREEN), "§7Warteschlange beigetreten", 1, 20, 10);
        player.openInventory(this.plugin.getKitManager().getInventory("1vs1-Warteschlange"));
    }
}
