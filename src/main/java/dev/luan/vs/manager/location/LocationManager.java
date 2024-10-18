package dev.luan.vs.manager.location;

import dev.luan.vs.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LocationManager {

    private final ConfigManager configManager;

    public LocationManager(final ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void teleportPlayer(final Player player, final String location_name) {
        final Location location = this.getLocation(location_name);
        if(location == null) return;
        player.teleport(location);
    }

    public void setLocation(final Player player, final String location_name) {
        final DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        final String locationString = location_name + ";" + player.getWorld().getName() + ";" + decimalFormat.format(player.getLocation().getX()) + ";" + decimalFormat.format(player.getLocation().getY()) + ";" + decimalFormat.format(player.getLocation().getZ()) + ";" + decimalFormat.format(player.getLocation().getYaw()) + ";" + decimalFormat.format(player.getLocation().getPitch());
        final List<String> locations = this.configManager.getStringList("Locations");
        locations.add(locationString.replace(",", "."));
        this.configManager.setStringList("Locations", locations);
    }

    public void removeLocation(final String location_name) {
        final List<String> locations = new ArrayList<>();
        for(final String locationString : this.configManager.getStringList("Locations")) {
            String[] strings = locationString.split(";");
            if(strings[0].equalsIgnoreCase(location_name)) continue;
            locations.add(locationString);
        }
        this.configManager.setStringList("Locations", locations);
    }

    public Location getLocation(final String location_name) {
        for(final String locationString : this.configManager.getStringList("Locations")) {
            String[] strings = locationString.split(";");
            if(!strings[0].equalsIgnoreCase(location_name)) continue;
            return new Location(Bukkit.getWorld(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3]), Double.parseDouble(strings[4]), Float.parseFloat(strings[5]), Float.parseFloat(strings[6]));
        }
        return null;
    }

    public boolean isInList(final String location_name) {
        if(this.getLocation(location_name) != null) return true;
        return false;
    }
}
