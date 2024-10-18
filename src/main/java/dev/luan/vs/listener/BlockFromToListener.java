package dev.luan.vs.listener;

import dev.luan.vs.VS;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromToListener implements Listener {

    private final VS plugin;

    public BlockFromToListener(final VS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void blockFromTo(final BlockFromToEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void blockDispense(final BlockDispenseEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void blockForm(final BlockFormEvent event) {
        event.setCancelled(true);
    }
}
