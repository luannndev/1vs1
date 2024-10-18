package dev.luan.vs.arena;

import dev.luan.vs.VS;
import dev.luan.vs.kits.KitManager;
import dev.luan.vs.messages.ServerMessage;
import dev.luan.vs.player.PlayerManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@Getter
public class ArenaManager {

    private final VS plugin;
    private final List<MapTemplate> mapTemplateDatas;
    private final HashMap<MapTemplate, List<DuelArena>> templateArenaDatas;
    private final List<DuelArena> duelArenaList;

    public ArenaManager(final VS plugin) {
        this.plugin = plugin;
        this.mapTemplateDatas = new ArrayList<>();
        this.duelArenaList = new ArrayList<>();
        this.templateArenaDatas = this.getTemplateArenas();
    }

    public boolean isInListTemplates(final String template) {
        return this.plugin.getConfigManager().getStringList("Templates").contains(template);
    }

    public boolean isInListArenas(final String template, final String arenaid) {
        return this.plugin.getConfigManager().getStringList(template.toLowerCase() + ".Arenas").contains(arenaid);
    }

    public HashMap<MapTemplate, List<DuelArena>> getTemplateArenas() {
        final HashMap<MapTemplate, List<DuelArena>> duelArenas = new HashMap<>();
        for(final String template : this.plugin.getConfigManager().getStringList("Templates")) {
            final MapTemplate mapTemplate = new MapTemplate(template, this.plugin.getConfigManager().getString(template + ".displayName"));
            final List<DuelArena> innerList = new ArrayList<>();

            for(final String arenaid : this.plugin.getConfigManager().getStringList(template + ".Arenas")) {
                ItemStack itemStack = new ItemStack(Material.matchMaterial(this.plugin.getConfigManager().getString(template + ".itemStack")));
                if(itemStack == null) {
                    itemStack = new ItemStack(Material.PAPER);
                }
                final String world_name = this.plugin.getConfigManager().getString(template + ".world");
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

                final List<Location> locations = new ArrayList<>();
                for(final String location_string : this.plugin.getConfigManager().getStringList(template + "." + arenaid + ".spawns")) {
                    final String[] split = location_string.split(";");
                    locations.add(new Location(world, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4])));
                }
                final String[] split = this.plugin.getConfigManager().getString(template + "." + arenaid + ".center").split(";");
                final Location center = new Location(world, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]));

                mapTemplate.setItemStack(itemStack);
                mapTemplate.setWorld(world);
                mapTemplate.setBorder(this.plugin.getConfigManager().getInteger(template + ".border"));

                final DuelArena duelArena = new DuelArena(mapTemplate, arenaid, locations, center);
                innerList.add(duelArena);
                this.duelArenaList.add(duelArena);
            }

            this.mapTemplateDatas.add(mapTemplate);
            duelArenas.put(mapTemplate, innerList);
        }

        return duelArenas;
    }

    public DuelArena getFreeDuelArena() {
        Collections.shuffle(this.duelArenaList);
        for(final DuelArena duelArena : this.duelArenaList) {
            if(duelArena.getArenaState() != ArenaState.LOBBY) continue;
            return duelArena;
        }
        return null;
    }

    public void searchArena(final Player player, final Player target, final KitManager.VSKit vsKit) {
        final PlayerManager.PlayerData playerData = this.plugin.getPlayerManager().getPlayerDatas().get(player);
        final Player targetPlayer = target;
        final PlayerManager.PlayerData targetPlayerData = this.plugin.getPlayerManager().getPlayerDatas().get(targetPlayer);
        final DuelArena duelArena = this.plugin.getArenaManager().getFreeDuelArena();
        this.plugin.getQueueManager().leaveQueue(player);
        this.plugin.getQueueManager().leaveQueue(targetPlayer);
        if(duelArena == null) {
            player.sendMessage(ServerMessage.getPrefix() + "§cEs konnte keine freie Arena gefunden werden!");
            playerData.getReceivedRequests().remove(targetPlayer);
            playerData.setScoreboard();
            return;
        }
        duelArena.setVSKit(vsKit);

        playerData.getReceivedRequests().clear();
        playerData.revokeSendedRequests();
        targetPlayerData.getReceivedRequests().clear();
        targetPlayerData.revokeSendedRequests();

        duelArena.arenaJoin(Arrays.asList(player, targetPlayer));
        playerData.setDuelArena(duelArena);
        targetPlayerData.setDuelArena(duelArena);
    }

    public List<DuelArena> getDuelArenas(final ArenaState arenaState) {
        final List<DuelArena> duelArenas = new ArrayList<>();
        for(final DuelArena duelArena : this.duelArenaList) {
            if(duelArena.getArenaState() != arenaState) continue;
            duelArenas.add(duelArena);
        }
        return duelArenas;
    }

    public List<DuelArena> getDuelArenas() {
        final List<DuelArena> duelArenas = new ArrayList<>();
        for(final DuelArena duelArena : this.duelArenaList) {
            duelArenas.add(duelArena);
        }
        return duelArenas;
    }

    @Getter
    public class DuelArena {

        private final MapTemplate mapTemplate;
        private final String arenaId;
        private final List<Player> players;
        private final HashMap<Player, Integer> points;
        private final List<Location> spawns;
        private final List<Block> placedBlocks;
        private final List<Item> droppedItems;
        private final Location center;
        @Setter
        private ArenaState arenaState;
        @Setter private Player winnerPlayer, looserPlayer;
        @Setter private KitManager.VSKit VSKit;

        public DuelArena(final MapTemplate mapTemplate, final String arenaId, final List<Location> spawns, final Location center) {
            this.mapTemplate = mapTemplate;
            this.arenaId = arenaId;
            this.spawns = spawns;
            this.players = new ArrayList<>();
            this.points = new HashMap<>();
            this.placedBlocks = new ArrayList<>();
            this.droppedItems = new ArrayList<>();
            this.center = center;
            this.setupArena();
        }

        public void setupArena() {
            this.players.clear();
            this.points.clear();
            this.winnerPlayer = null;
            this.looserPlayer = null;
            this.arenaState = ArenaState.LOBBY;
            this.VSKit = null;
            for(final Block block : this.placedBlocks) {
                if(block == null) continue;
                block.setType(Material.AIR);
            }
            this.placedBlocks.clear();
            for(final Item item : this.droppedItems) {
                if(item == null) continue;
                item.remove();
            }
            this.droppedItems.clear();
        }

        public void arenaJoin(final List<Player> players) {
            this.arenaState = ArenaState.STARTING;
            players.forEach(player -> {
                player.getInventory().clear();
                this.players.add(player);
                this.points.put(player, 0);
                plugin.getStatsManager().addStat(player.getUniqueId(), player.getName(), "matches", 1);
            });
            this.startRound();
        }

        BukkitTask bukkitTask = null;
        int i = 4;
        public void startRound() {
            this.arenaState = ArenaState.STARTING;
            for(final Block block : this.placedBlocks) {
                if(block == null) continue;
                block.setType(Material.AIR);
            }
            this.placedBlocks.clear();
            this.setScoreboard();
            this.teleportPlayers();
            this.bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if(i >= 0) {
                    this.players.forEach(player -> {
                        player.sendTitle("§a" + (i <= 0 ? "GO!" : i), "", 1, 40, 20);
                    });
                    if(i <= 0) {
                        this.arenaState = ArenaState.INGAME;
                        this.bukkitTask.cancel();
                        i = 4;
                    }
                }
                --i;
            }, 10L, 20L);
        }

        public void arenaLeave(final Player player, final boolean serverQuit) {
            if(serverQuit) {
                if(this.players.contains(player)) {
                    this.players.remove(player);
                    this.setLooserPlayer(player);
                    this.endRound();
                    plugin.getStatsManager().addStat(player.getUniqueId(), player.getName(), "loses", 1);
                    plugin.getStatsManager().addStat(player.getUniqueId(), player.getName(), "deaths", 1);
                }
            } else {
                this.setLooserPlayer(player);
                this.endRound();
            }
        }

        public void playerDeath(final Player player) {
            plugin.getStatsManager().addStat(player.getUniqueId(), player.getName(), "deaths", 1);
            for(final Player players : this.players) {
                if(players.equals(player)) continue;
                this.points.put(players, this.points.get(players) + 1);
            }
            if(this.getWinnerPlayer() != null) {
                this.endRound();
                return;
            }
            this.startRound();
        }

        public Player getWinnerPlayer() {
            if(this.winnerPlayer == null) {
                if(this.players.size() <= 1) return this.players.get(0);
                if(this.looserPlayer != null) {
                    for(final Player player : this.players) {
                        if(player.equals(this.looserPlayer)) continue;
                        this.winnerPlayer = player;
                    }
                }
                for(final Player player : this.players) {
                    if(this.points.get(player) < 3) continue;
                    this.winnerPlayer = player;
                    break;
                }
            }
            return this.winnerPlayer;
        }

        public Player getLooserPlayer() {
            if(this.looserPlayer == null) {
                for(final Player player : this.players) {
                    if(player.equals(getWinnerPlayer())) continue;
                    this.looserPlayer = player;
                }
            }
            return this.looserPlayer;
        }

        public void endRound() {
            if(this.bukkitTask != null) this.bukkitTask.cancel();
            this.arenaState = ArenaState.END;
            final Player winnerPlayer = this.getWinnerPlayer();
            this.players.forEach(player -> {
                final int points = this.points.get(player);
                if(player.equals(winnerPlayer)) {
                    player.sendMessage(" \n" + ServerMessage.getPrefix() + "§aGEWONNEN! §7Du hast §f" + this.getLooserPlayer().getName() + " §7im 1vs1 besiegt.\n ");
                    plugin.getStatsManager().addStat(player.getUniqueId(), player.getName(), "wins", 1);
                    if(points > 0) plugin.getStatsManager().addStat(player.getUniqueId(), player.getName(), "kills", points);
                } else {
                    player.teleport(this.center);
                    player.sendMessage(" \n" + ServerMessage.getPrefix() + "§cVERLOREN! §7Du wurdest von §f" + winnerPlayer.getName() + " §7im 1vs1 besiegt.\n ");
                    plugin.getStatsManager().addStat(player.getUniqueId(), player.getName(), "loses", 1);
                    if(points > 0) plugin.getStatsManager().addStat(player.getUniqueId(), player.getName(), "kills", points);
                }
                plugin.getPlayerManager().resetPlayer(player);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    final PlayerManager.PlayerData playerData = plugin.getPlayerManager().getPlayerDatas().get(player);
                    plugin.getPlayerManager().resetPlayer(player);
                    playerData.setDuelArena(null);
                    playerData.setScoreboard();
                    playerData.setLobbyItems();
                    plugin.getLocationManager().teleportPlayer(player, "spawn");
                    player.setFireTicks(0);
                    player.setArrowsInBody(0);
                }, 20L);
            });
            for(final Player spectatorPlayer : plugin.getPlayerManager().getSpectatorPlayers().keySet()) {
                if(!plugin.getPlayerManager().getSpectatorPlayers().get(spectatorPlayer).equals(this)) continue;
                plugin.getPlayerManager().removeSpectator(spectatorPlayer);
            }
            this.setupArena();
        }

        public void teleportPlayers() {
            int current = 0;
            for(final Player player : this.players) {
                player.teleport(this.spawns.get(current));
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                if(this.VSKit.getKey().equalsIgnoreCase("only_sword")) {
                    player.setMaxHealth(200);
                }
                player.setHealth(player.getMaxHealth());
                player.getInventory().setContents(this.VSKit.getItemStacks());
                player.getInventory().setArmorContents(this.VSKit.getArmorContents());
                player.setFireTicks(0);
                player.setArrowsInBody(0);
                ++current;
            }
        }

        public void sendMessage(final String message) {
            this.players.forEach(player -> {
                player.sendMessage(message);
            });
        }

        public void setScoreboard() {
            for(final Player player : this.players) {
                final PlayerManager.PlayerData playerData = plugin.getPlayerManager().getPlayerDatas().get(player);
                playerData.getScoreboardAPI().updateLines(
                        " ",
                        "§c" + this.points.get(this.players.get(0)) + " §8- §7" + this.players.get(0).getName() + " ",
                        "§c" + this.points.get(this.players.get(1)) + " §8- §7" + this.players.get(1).getName() + " ",
                        " ",
                        "§7Map§8: §f" + this.mapTemplate.getDisplayName());
            }
        }
    }

    @Getter @Setter
    public static class MapTemplate {

        final String name, displayName;
        ItemStack itemStack;
        World world;
        int border;
        public MapTemplate(final String name, final String displayName) {
            this.name = name;
            this.displayName = displayName;
        }
    }

    public enum ArenaState {
        LOBBY, STARTING, INGAME, END
    }
}
