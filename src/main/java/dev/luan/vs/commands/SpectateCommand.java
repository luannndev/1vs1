package dev.luan.vs.commands;

import dev.luan.vs.VS;
import dev.luan.vs.arena.ArenaManager;
import dev.luan.vs.messages.ServerMessage;
import dev.luan.vs.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand implements CommandExecutor {

    private final VS plugin;

    public SpectateCommand(final VS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        if(args.length >= 1) {
            final Player player = (Player) sender;
            if(this.plugin.getPlayerManager().getPlayerDatas().get(player).getDuelArena() != null) {
                sender.sendMessage(ServerMessage.getPrefix() + "§cDu kannst im Spiel keine Spieler beobachten!");
                return true;
            }
            String arg = args[0];
            ArenaManager.DuelArena finalDuelArena = null;
            final Player target = Bukkit.getPlayer(arg);
            if(target == null || !target.isOnline()) {
                for(final ArenaManager.DuelArena duelArena : this.plugin.getArenaManager().getDuelArenas()) {
                    if(!duelArena.getArenaId().equalsIgnoreCase(arg)) continue;
                    finalDuelArena = duelArena;
                }
                if(finalDuelArena == null) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Arena existiert nicht!");
                    return true;
                }
            } else {
                final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(target);
                if(playerData.getDuelArena() == null) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDer Spieler ist in keinem Spiel!");
                    return true;
                }
                finalDuelArena = playerData.getDuelArena();
            }

            if(finalDuelArena.getArenaState() == ArenaManager.ArenaState.LOBBY) {
                sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Arena ist nicht im Spiel!");
                return true;
            }
            if(this.plugin.getPlayerManager().getSpectatorPlayers().containsKey(player) && this.plugin.getPlayerManager().getSpectatorPlayers().get(player).equals(finalDuelArena)) {
                sender.sendMessage(ServerMessage.getPrefix() + "§cDu bist bereits in dieser Arena!");
                return true;
            }

            this.plugin.getPlayerManager().setSpectator(player, finalDuelArena);
            return true;
        }
        sender.sendMessage(ServerMessage.getPrefix() + "§c/spectate <Spieler/ArenaId>");
        return false;
    }
}
