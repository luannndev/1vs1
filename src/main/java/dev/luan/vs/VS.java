package dev.luan.vs;

import dev.luan.vs.config.ConfigManager;
import dev.luan.vs.database.DatabaseManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class VS extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.databaseManager = new DatabaseManager(this.configManager.getString("database.hostname"), this.configManager.getInteger("database.port"), this.configManager.getString("database.database"), this.configManager.getString("database.user"), this.configManager.getString("database.password"));

    }

    @Override
    public void onDisable() {

    }
}
