package dev.luan.vs.commands;

import dev.luan.vs.VS;
import dev.luan.vs.messages.ServerMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    private final VS plugin;

    public BuildCommand(final VS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(!(sender instanceof Player)) return true;
        if(!sender.hasPermission("1vs1.commands.build")) return true;

        Player player = (Player)sender;
        if(args.length >= 1) {
            player = Bukkit.getPlayer(args[0]);
            if(player == null) {
                sender.sendMessage(ServerMessage.getPrefix() + "§cDer Spieler " + args[0] + " §cist offline!");
                return true;
            }
        }
        if(!this.plugin.getPlayerManager().getBuildPlayers().contains(player)) {
            player.setGameMode(GameMode.CREATIVE);
            this.plugin.getPlayerManager().getBuildPlayers().add(player);
            player.sendMessage(ServerMessage.getPrefix() + "§7Du kannst nun §ebauen");
            if(player != sender) {
                sender.sendMessage(ServerMessage.getPrefix() + "§7Der Spieler kann nun §ebauen");
            }
            return true;
        }
        this.plugin.getPlayerManager().getBuildPlayers().remove(player);
        player.sendMessage(ServerMessage.getPrefix() + "§cDu kannst nun nicht mehr §ebauen");
        if(player != sender) {
            sender.sendMessage(ServerMessage.getPrefix() + "§cDer Spieler kann nun nicht mehr §ebauen");
        }
        return false;
    }

}