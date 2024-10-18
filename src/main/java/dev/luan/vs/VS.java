package dev.luan.vs;

import dev.luan.vs.arena.ArenaManager;
import dev.luan.vs.arena.queue.QueueManager;
import dev.luan.vs.commands.*;
import dev.luan.vs.config.ConfigManager;
import dev.luan.vs.database.DatabaseManager;
import dev.luan.vs.database.gamerank.GameRankManager;
import dev.luan.vs.database.stats.StatsManager;
import dev.luan.vs.database.stats.StatsMenu;
import dev.luan.vs.database.top.TopManager;
import dev.luan.vs.kits.KitManager;
import dev.luan.vs.listener.*;
import dev.luan.vs.manager.inventory.InventoryManager;
import dev.luan.vs.manager.location.LocationManager;
import dev.luan.vs.player.PlayerManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class VS extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private StatsManager statsManager;
    private GameRankManager gameRankManager;
    private StatsMenu statsMenu;
    private TopManager topManager;

    private ArenaManager arenaManager;
    private PlayerManager playerManager;
    private LocationManager locationManager;
    private InventoryManager inventoryManager;
    private QueueManager queueManager;
    private KitManager kitManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.databaseManager = new DatabaseManager(this.configManager.getString("database.hostname"), this.configManager.getInteger("database.port"), this.configManager.getString("database.database"), this.configManager.getString("database.user"), this.configManager.getString("database.password"));
        this.statsManager = new StatsManager(this.databaseManager, "1vs1_stats");
        this.gameRankManager = new GameRankManager(this);
        this.statsMenu = new StatsMenu(this);

        this.arenaManager = new ArenaManager(this);
        this.playerManager = new PlayerManager(this);
        this.locationManager = new LocationManager(this.configManager);
        this.topManager = new TopManager(this);
        this.inventoryManager = new InventoryManager(this);
        this.kitManager = new KitManager(this);
        this.queueManager = new QueueManager(this);

        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BlockBreakListener(this), this);
        pluginManager.registerEvents(new BlockExplodeListener(), this);
        pluginManager.registerEvents(new BlockFromToListener(this), this);
        pluginManager.registerEvents(new BlockPlaceListener(this), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(this), this);
        pluginManager.registerEvents(new EntityDamageListener(this), this);
        pluginManager.registerEvents(new InventoryClickListener(this), this);
        pluginManager.registerEvents(new PlayerArmorStandManipulateListener(this), this);
        pluginManager.registerEvents(new PlayerDeathListener(this), this);
        pluginManager.registerEvents(new PlayerDropItemListener(this), this);
        pluginManager.registerEvents(new PlayerInteractListener(this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerMoveListener(this), this);
        pluginManager.registerEvents(new PlayerPickupItemListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new WeatherChangeListener(), this);

        this.getCommand("build").setExecutor(new BuildCommand(this));
        this.getCommand("duel").setExecutor(new DuelCommand(this));
        this.getCommand("leave").setExecutor(new LeaveCommand(this));
        this.getCommand("location").setExecutor(new LocationCommand(this));
        this.getCommand("spectate").setExecutor(new SpectateCommand(this));
        this.getCommand("stats").setExecutor(new StatsCommand(this));
        this.getCommand("1vs1").setExecutor(new VSCommand(this));
        this.getCommand("world").setExecutor(new WorldCommand());

        this.runScheduler();
    }

    @Override
    public void onDisable() {
        this.queueManager.getEntityDatas().values().forEach(armorStand -> {
            armorStand.remove();
        });
        this.topManager.removeTopHologram();
        Bukkit.getOnlinePlayers().forEach(players -> {
            players.kickPlayer("§cRestart server");
        });
    }

    int i = 60;
    long currentTimeMillis = System.currentTimeMillis();
    private void runScheduler() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            this.playerManager.getPlayerDatas().values().forEach(lobbyPlayer -> {
                lobbyPlayer.setScoreboardDisplayName();
            });
            if(System.currentTimeMillis() >= currentTimeMillis + 1000) {
                this.statsManager.checkSeasonalStats();
                this.statsManager.checkTemporaryStats();
                this.inventoryManager.updateInventory(InventoryManager.PublicInventory.RUNNING_GAMES);
                this.queueManager.getQueueTask();
                if(i <= 0) {
                    this.topManager.updateHologram();
                    this.inventoryManager.updateInventory(InventoryManager.PublicInventory.LEADERBOARDS);
                    i = 60;
                }
                i--;
                currentTimeMillis = System.currentTimeMillis();
            }
        }, 0L, 10L);
    }
}
