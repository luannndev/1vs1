package dev.luan.vs.commands;

import dev.luan.vs.VS;
import dev.luan.vs.messages.ServerMessage;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocationCommand implements CommandExecutor {

    private final VS plugin;

    public LocationCommand(final VS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        if(!sender.hasPermission("1vs1.commands.location")) return true;
        final Player player = (Player) sender;
        if(args.length >= 1 && args.length <= 2) {
            if(args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {
                if(args.length != 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/location teleport <Location>");
                    return true;
                }
                final Location location = this.plugin.getLocationManager().getLocation(args[1]);
                if(location == null) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Location existiert nicht!");
                    return true;
                }
                player.teleport(location);
                sender.sendMessage(ServerMessage.getPrefix() + "§7Du wurdest zur Location §f" + args[1] + " §7teleportiert.");
                return true;
            }
            if(args[0].equalsIgnoreCase("set")) {
                if(args.length != 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/location set <Location>");
                    return true;
                }
                final String location_name = args[1];
                if(this.plugin.getLocationManager().isInList(location_name)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Location existiert bereits!");
                    return true;
                }

                this.plugin.getLocationManager().setLocation(player, location_name);
                sender.sendMessage(ServerMessage.getPrefix() + "§7Du hast die Location " + args[1] + " gesetzt.");
                return true;
            }
            if(args[0].equalsIgnoreCase("remove")) {
                if(args.length != 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/location remove <Location>");
                    return true;
                }
                final String location_name = args[1];
                if(!this.plugin.getLocationManager().isInList(location_name)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Location existiert nicht!");
                    return true;
                }

                this.plugin.getLocationManager().removeLocation(location_name);
                sender.sendMessage(ServerMessage.getPrefix() + "§cDu hast die Location §e" + args[1] + " §centfernt.");
                return true;
            }
            this.sendHelp(sender);
            return true;
        }
        this.sendHelp(sender);
        return false;
    }

    private void sendHelp(final CommandSender sender) {
        sender.sendMessage(" \n§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊§8┃ " + ServerMessage.getString("Locations", ServerMessage.MessageColor.YELLOW) + " §8┃§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊");
        sender.sendMessage(" §7Location setzen §8» §f/location set <Location>");
        sender.sendMessage(" §7Location entfernen §8» §f/location remove <Location>");
        sender.sendMessage(" §7Zu Location teleportieren §8» §f/location teleport <Location>");
        sender.sendMessage("§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊§8┃ " + ServerMessage.getString("Locations", ServerMessage.MessageColor.YELLOW) + " §8┃§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊\n ");
    }
}
