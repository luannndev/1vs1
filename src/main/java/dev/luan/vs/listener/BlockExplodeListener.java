package dev.luan.vs.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockExplodeListener implements Listener {

    @EventHandler
    public void blockExplode(final BlockExplodeEvent event) {
        event.blockList().clear();
    }

    @EventHandler
    public void explode(final EntityExplodeEvent event) {
        event.blockList().clear();
    }
}

