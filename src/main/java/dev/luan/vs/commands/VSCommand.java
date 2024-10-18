package dev.luan.vs.commands;

import dev.luan.vs.VS;
import dev.luan.vs.arena.ArenaManager;
import dev.luan.vs.messages.ServerMessage;
import dev.luan.vs.utilities.RandomStringGenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class VSCommand implements CommandExecutor {

    private final VS plugin;

    public VSCommand(final VS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        if(!sender.hasPermission("1vs1.commands.setup")) return true;
        final Player player = (Player) sender;
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("freearena")) {
                final ArenaManager.DuelArena duelArena = this.plugin.getArenaManager().getFreeDuelArena();
                if(duelArena == null) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cEs konnte keine freie Arena gefunden werden!");
                    return true;
                }
                sender.sendMessage(ServerMessage.getPrefix() + "§7Freie Arena gefunden! §f" + duelArena.getArenaId() + " §7(" + duelArena.getMapTemplate().getDisplayName() + ")");
                return true;
            }
            if(args[0].equalsIgnoreCase("list")) {
                final List<String> templates = this.plugin.getConfigManager().getStringList("Templates");
                sender.sendMessage(" \n§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊§8┃ " + ServerMessage.getString("1vs1 Maps", ServerMessage.MessageColor.YELLOW) + " §8┃§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊");
                for(final String template : templates) {
                    sender.sendMessage("§8» §7" + template);
                }
                sender.sendMessage(" ");
                return true;
            }
            if(args[0].equalsIgnoreCase("arenas")) {
                if(args.length != 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/1vs1 arenas <Map>");
                    return true;
                }
                final String template = args[1].toLowerCase();
                if(!this.plugin.getArenaManager().isInListTemplates(template)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Map existiert nicht!");
                    return true;
                }

                sender.sendMessage(" \n§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊§8┃ " + ServerMessage.getString("1vs1 Arenas", ServerMessage.MessageColor.YELLOW) + " §8(§7" + template + "§8) §8┃§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊");
                for(final ArenaManager.DuelArena duelArena : this.plugin.getArenaManager().getDuelArenas()) {
                    sender.sendMessage("§8» §7" + duelArena.getArenaId() + " §8| §7Status §8» §f" + duelArena.getArenaState().toString());
                }
                sender.sendMessage(" ");
                return true;
            }
            if(args[0].equalsIgnoreCase("addmap")) {
                if(args.length != 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/1vs1 addmap <Map>");
                    return true;
                }
                final String template = args[1].toLowerCase();
                if(this.plugin.getArenaManager().isInListTemplates(template)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Map existiert bereits!");
                    return true;
                }

                final List<String> templates = this.plugin.getConfigManager().getStringList("Templates");
                templates.add(template);
                this.plugin.getConfigManager().setStringList("Templates", templates);
                this.plugin.getConfigManager().setString(template + ".displayName", template);
                this.plugin.getConfigManager().setString(template + ".itemStack", "PAPER");
                this.plugin.getConfigManager().setInteger(template + ".border", 20);
                this.plugin.getConfigManager().setStringList(template + ".Arenas", Collections.emptyList());
                sender.sendMessage(ServerMessage.getPrefix() + "§7Du hast die Map §f" + template + " §ahinzugefügt.");
                return true;
            }

            if(args[0].equalsIgnoreCase("setborder")) {
                if (args.length <= 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/1vs1 setborder <Map> <Radius>");
                    return true;
                }
                final String template = args[1].toLowerCase();
                if (!this.plugin.getArenaManager().isInListTemplates(template)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Map existiert nicht!");
                    return true;
                }

                int radius;
                try {
                    radius = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDie Angabe muss einem Zahlenwert entsprechen!");
                    return true;
                }
                sender.sendMessage(ServerMessage.getPrefix() + "§7Du hast den Radius für die Map §f" + template + " §7auf §f" + radius + " §agesetzt.");
                this.plugin.getConfigManager().setInteger(template + ".border", radius);
                return true;
            }

            if(args[0].equalsIgnoreCase("setdisplayname")) {
                if (args.length <= 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/1vs1 setdisplayname <Map> <Name>");
                    return true;
                }
                final String template = args[1].toLowerCase();
                if (!this.plugin.getArenaManager().isInListTemplates(template)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Map existiert nicht!");
                    return true;
                }

                final StringBuilder stringBuilder = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    if(i == args.length-1) {
                        stringBuilder.append(args[i]);
                    } else {
                        stringBuilder.append(args[i]).append(" ");
                    }
                }

                this.plugin.getConfigManager().setString(template + ".displayName", stringBuilder.toString());
                sender.sendMessage(ServerMessage.getPrefix() + "§7Du hast den Namen von der Map §f" + template + " §7zu §f" + stringBuilder.toString() + " §ageändert.");
                return true;
            }

            if(args[0].equalsIgnoreCase("addarena")) {
                if(args.length != 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/1vs1 addarena <Map>");
                    return true;
                }
                final String template = args[1].toLowerCase();
                if(!this.plugin.getArenaManager().isInListTemplates(template)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Map existiert nicht!");
                    return true;
                }
                final String arenaId = RandomStringGenerator.generateRandomString(5, RandomStringGenerator.Mode.ALPHANUMERICUPPERCASE);
                final List<String> arenas = this.plugin.getConfigManager().getStringList(template + ".Arenas");
                arenas.add(arenaId);
                this.plugin.getConfigManager().setStringList(template + ".Arenas", arenas);
                sender.sendMessage(ServerMessage.getPrefix() + "§7Du hast die Arena §f" + arenaId + " §7zur Map §f" + template + " §ahinzugefügt.");
                return true;
            }

            if(args[0].equalsIgnoreCase("addspawn")) {
                if (args.length != 3) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/1vs1 addspawn <Map> <ArenaId>");
                    return true;
                }
                final String template = args[1].toLowerCase();
                if(!this.plugin.getArenaManager().isInListTemplates(template)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Map existiert nicht!");
                    return true;
                }
                final String arenaid = args[2].toUpperCase();
                if (!this.plugin.getArenaManager().isInListArenas(template, arenaid)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese ArenaId existiert nicht!");
                    return true;
                }
                if(this.plugin.getConfigManager().getString(template + ".world") == null) {
                    this.plugin.getConfigManager().setString(template + ".world", player.getWorld().getName());
                }
                final List<String> locations = this.plugin.getConfigManager().getStringList(template + "." + arenaid + ".spawns");
                if(locations.size() >= 2) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDu kannst nur maximal 2 Spawns hinzufügen!");
                    return true;
                }
                final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
                final String location_string = decimalFormat.format(player.getLocation().getX()) + ";" + decimalFormat.format(player.getLocation().getY()) + ";" + decimalFormat.format(player.getLocation().getZ()) + ";" + decimalFormat.format(player.getLocation().getYaw()) + ";" + decimalFormat.format(player.getLocation().getPitch());
                locations.add(location_string.replaceAll(",", "."));
                this.plugin.getConfigManager().setStringList(template + "." + arenaid + ".spawns", locations);
                sender.sendMessage(ServerMessage.getPrefix() + "§7Du einen Spawn für die Arena §f" + arenaid + " §ahinzugefügt.");
                return true;
            }

            if(args[0].equalsIgnoreCase("setcenter")) {
                if (args.length != 3) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§c/1vs1 setcenter <Map> <ArenaId>");
                    return true;
                }
                final String template = args[1].toLowerCase();
                if(!this.plugin.getArenaManager().isInListTemplates(template)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese Map existiert nicht!");
                    return true;
                }
                final String arenaid = args[2].toUpperCase();
                if (!this.plugin.getArenaManager().isInListArenas(template, arenaid)) {
                    sender.sendMessage(ServerMessage.getPrefix() + "§cDiese ArenaId existiert nicht!");
                    return true;
                }
                if(this.plugin.getConfigManager().getString(template + ".world") == null) {
                    this.plugin.getConfigManager().setString(template + ".world", player.getWorld().getName());
                }
                final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
                final String location_string = decimalFormat.format(player.getLocation().getX()) + ";" + decimalFormat.format(player.getLocation().getY()) + ";" + decimalFormat.format(player.getLocation().getZ()) + ";" + decimalFormat.format(player.getLocation().getYaw()) + ";" + decimalFormat.format(player.getLocation().getPitch());
                this.plugin.getConfigManager().setString(template + "." + arenaid + ".center", location_string.replaceAll(",", "."));
                sender.sendMessage(ServerMessage.getPrefix() + "§7Du das Center für die Arena §f" + arenaid + " §ahinzugefügt.");
                return true;
            }
            this.sendHelp(sender);
            return true;
        }
        this.sendHelp(sender);
        return false;
    }

    public void sendHelp(final CommandSender sender) {
        sender.sendMessage(" \n§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊§8┃ " + ServerMessage.getString("1vs1", ServerMessage.MessageColor.YELLOW) + " §8┃§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊");
        sender.sendMessage(" §71vs1 Maps §8» §f/1vs1 list");
        sender.sendMessage(" §7Arenalist von Maps §8» §f/1vs1 arenas <Map>");
        sender.sendMessage(" §7Map hinzufügen §8» §f/1vs1 addmap <Name>");
        sender.sendMessage(" §7Anzeigename ändern §8» §f/1vs1 setdisplayname <Map> <Name>");
        sender.sendMessage(" §7Border setzen §8» §f/1vs1 setborder <Map> <Radius>");
        sender.sendMessage(" §7Arena hinzufügen §8» §f/1vs1 addarena <Map>");
        sender.sendMessage(" §7Spawn setzen §8» §f/1vs1 addspawn <Map> <ArenaId>");
        sender.sendMessage(" §7Center setzen §8» §f/1vs1 setcenter <Map> <ArenaId>");
        sender.sendMessage("§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊§8┃ " + ServerMessage.getString("1vs1", ServerMessage.MessageColor.YELLOW) + " §8┃§8⚊⚊⚊⚊⚊⚊⚊⚊⚊⚊\n ");
    }
}
