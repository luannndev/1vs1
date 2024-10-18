package dev.luan.vs.commands;

import dev.luan.vs.messages.ServerMessage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        if(!sender.hasPermission("1vs1.commands.world")) return true;
        final Player player = (Player) sender;
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(" \n§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊§8┃ " + ServerMessage.getString("Worlds", ServerMessage.MessageColor.YELLOW) + " §8┃§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊");
                Bukkit.getWorlds().forEach(world -> {
                    sender.sendMessage(" §8» §7" + world.getName());
                });
                return true;
            }
            if(args[0].equalsIgnoreCase("import")) {
                if(args.length != 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/world import <Worldname>");
                    return true;
                }
                final String world_name = args[1];
                try {
                    final WorldCreator worldCreator = new WorldCreator(world_name);
                    Bukkit.getServer().createWorld(worldCreator);
                    final World world = Bukkit.getServer().getWorld(world_name);
                    world.setThundering(false);
                    world.setStorm(false);
                    world.setTime(5000);
                    world.setGameRuleValue("randomTickSpeed", "0");
                    world.setGameRuleValue("doTileDrops", "false");
                    world.setGameRuleValue("doMobSpawning", "false");
                    world.setGameRuleValue("doDaylightCycle", "false");
                    world.setThundering(false);
                    world.setStorm(false);

                    sender.sendMessage(ServerMessage.getPrefix() + "§7Die Welt §f" + world_name + " §7wurde importiert.");
                } catch (Exception e) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDie Welt konnte nicht importiert werden!");
                    return true;
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {
                if(args.length != 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/world teleport <World>");
                    return true;
                }
                final String world_name = args[1];
                final World world = Bukkit.getWorld(world_name);
                if(world == null) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDie Welt existiert nicht!");
                    return true;
                }
                player.teleport(world.getSpawnLocation());
                sender.sendMessage(ServerMessage.getPrefix() + "§7Du wurdest in die Welt §f" + world.getName() + " §7teleportiert.");
                return true;
            }
            return true;
        }
        this.sendHelp(sender);
        return false;
    }

    private void sendHelp(final CommandSender sender) {
        sender.sendMessage(" \n§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊§8┃ " + ServerMessage.getString("Worlds", ServerMessage.MessageColor.YELLOW) + " §8┃§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊");
        sender.sendMessage(" §7Importierte Welten §8» §f/world list");
        sender.sendMessage(" §7Welt importieren §8» §f/world import <Worldname>");
        sender.sendMessage(" §7Zur Welt teleportieren §8» §f/world teleport <World>");
        sender.sendMessage("§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊§8┃ " + ServerMessage.getString("Worlds", ServerMessage.MessageColor.YELLOW) + " §8┃§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊\n ");
    }
}
