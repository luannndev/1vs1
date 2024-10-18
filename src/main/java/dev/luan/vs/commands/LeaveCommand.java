package dev.luan.vs.commands;

import dev.luan.vs.VS;
import dev.luan.vs.messages.ServerMessage;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {

    private final VS plugin;

    public LeaveCommand(final VS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        final Player player = (Player) sender;
        final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
        if(playerData.getDuelArena() == null) {
            sender.sendMessage(ServerMessage.getPrefix() + "§cDu bist in keinem Spiel!");
            return true;
        }
        playerData.getDuelArena().arenaLeave(player, false);
        return false;
    }
}
