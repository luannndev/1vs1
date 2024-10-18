package dev.luan.vs.config;

import dev.luan.vs.VS;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ConfigManager {

    private final VS plugin;

    public ConfigManager(final VS plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public void delete(final String name) {
        this.plugin.getConfig().set(name, null);
    }

    public int getInteger(final String config) {
        return this.plugin.getConfig().getInt("Config." + config);
    }

    public boolean getBoolean(final String config) {
        return this.plugin.getConfig().getBoolean("Config." + config);
    }

    public String getString(final String config) {
        return this.plugin.getConfig().getString("Config." + config);
    }

    public List<String> getStringList(final String config) { return this.plugin.getConfig().getStringList("Config." + config); }

    public HashSet<String> getList(final String config) {
        final HashSet<String> regions = new HashSet<String>();
        final Iterator<String> iterator = this.plugin.getConfig().getStringList("Config." + config).iterator();
        while (iterator.hasNext()) {
            regions.add(iterator.next().toString());
        }
        return regions;
    }

    public void setInteger(final String config, final int value) {
        this.plugin.getConfig().set("Config." + config, value);
        this.plugin.saveConfig();
    }

    public void setString(final String config, final String value) {
        this.plugin.getConfig().set("Config." + config, value);
        this.plugin.saveConfig();
    }

    public void setBoolean(final String config, final boolean value) {
        this.plugin.getConfig().set("Config." + config, value);
        this.plugin.saveConfig();
    }

    public void setStringList(final String config, final List<String> value) {
        this.plugin.getConfig().set("Config." + config, value);
        this.plugin.saveConfig();
    }
}
