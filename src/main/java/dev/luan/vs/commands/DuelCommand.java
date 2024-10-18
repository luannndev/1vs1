package dev.luan.vs.commands;

import dev.luan.vs.VS;
import dev.luan.vs.kits.KitManager;
import dev.luan.vs.messages.ServerMessage;
import dev.luan.vs.player.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DuelCommand implements CommandExecutor {

    private final VS plugin;
    private final HashMap<Player, Long> cooldown;

    public DuelCommand(final VS plugin) {
        this.plugin = plugin;
        this.cooldown = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        final Player player = (Player) sender;
        final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
        if(args.length == 0) {
            sender.sendMessage(ServerMessage.getPrefix() + "§c/duel <Name>");
            return true;
        }
        if(playerData.getDuelArena() != null || this.plugin.getPlayerManager().getSpectatorPlayers().containsKey(player)) {
            sender.sendMessage(ServerMessage.getPrefix() + "§cDu kannst nur in der Lobby Spieler herausfordern!");
            return true;
        }
        if(this.cooldown.containsKey(player) && System.currentTimeMillis() < this.cooldown.get(player)) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 2);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cBitte warte einen Moment!"));
            return true;
        }
        final String player_name = args[0];
        if(player_name.equalsIgnoreCase(player.getName())) {
            sender.sendMessage(ServerMessage.getPrefix() + "§cDu kannst dich nicht selbst herausfordern!");
            return true;
        }
        final Player targetPlayer = Bukkit.getPlayer(player_name);
        final PlayerManager.PlayerData targetPlayerData = this.plugin.getPlayerManager().getPlayerDatas().get(targetPlayer);
        if(targetPlayer == null || !targetPlayer.isOnline()) {
            sender.sendMessage(ServerMessage.getPrefix() + "§cDieser Spieler ist offline!");
            return true;
        }
        if(targetPlayerData.getDuelArena() != null) {
            sender.sendMessage(ServerMessage.getPrefix() + "§cDer Spieler ist bereits in einem Spiel!");
            return true;
        }

        if(targetPlayerData.getReceivedRequests().contains(player)) {
            targetPlayer.sendMessage(ServerMessage.getPrefix() + "§c" + player.getName() + " §chat die Anfrage zurückgezogen.");
            player.sendMessage(ServerMessage.getPrefix() + "§cDu hast die Anfrage an " + targetPlayer.getName() + " §czurückgezogen.");
            targetPlayerData.getReceivedRequests().remove(player);
            targetPlayerData.setScoreboard();
            return true;
        }

        if(playerData.getReceivedRequests().contains(targetPlayer)) {
            this.plugin.getArenaManager().searchArena(player, targetPlayer, targetPlayerData.getVSKit());
            return true;
        }

        if(args.length < 2) {
            player.openInventory(this.plugin.getKitManager().getInventory(targetPlayer.getName()));
            return true;
        }

        final String kit_key = args[1];
        final KitManager.VSKit vsKit = this.plugin.getKitManager().getVSKitDatas().get(kit_key);
        if(vsKit == null) {
            player.sendMessage(ServerMessage.getPrefix() + "§cDieses Kit existiert nicht!");
            return true;
        }
        this.cooldown.put(player, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1));

        targetPlayerData.getReceivedRequests().add(player);
        targetPlayerData.setScoreboard();
        playerData.setVSKit(vsKit);
        player.sendMessage(ServerMessage.getPrefix() + "§7Du hast §f" + targetPlayer.getName() + " §7zu einem Match herausgefordert. §8(§7Kit: " + vsKit.getDisplayName() + "§8)");
        targetPlayer.sendMessage(ServerMessage.getPrefix() + "§7Du wurdest von §f" + player.getName() + " §7zu einem Match herausgefordert. §8(§7Kit: " + vsKit.getDisplayName() + "§8)");
        final TextComponent textComponent = new TextComponent("§8[§aANNEHMEN§8]");
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel " + player.getName()));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§7Match-Herausforderung §aaanehmen")).create()));
        targetPlayer.spigot().sendMessage(new TextComponent(" §7Klick' um die Anfrage anzunehmen. "), textComponent);
        return false;
    }
}
