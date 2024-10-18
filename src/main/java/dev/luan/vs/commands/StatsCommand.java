package dev.luan.vs.commands;

import dev.luan.vs.VS;
import dev.luan.vs.messages.ServerMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StatsCommand implements CommandExecutor {

    private final VS plugin;

    public StatsCommand(final VS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        final Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        if(args.length >= 1) {
            final String player_name = args[0];
            if(!this.plugin.getStatsManager().isInList(player_name)) {
                sender.sendMessage(ServerMessage.getPrefix() + "§cDer Spieler hat bisher noch kein 1vs1 gespielt!");
                return true;
            }
            uuid = this.plugin.getStatsManager().getUniqueId(player_name);
        }

        player.openInventory(this.plugin.getStatsMenu().getStatMenu(uuid));
        return false;
    }
}
