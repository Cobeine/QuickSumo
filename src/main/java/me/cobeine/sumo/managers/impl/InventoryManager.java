package me.cobeine.sumo.managers.impl;

import me.cobeine.sumo.managers.InventorySaver;
import me.cobeine.sumo.utils.Interfaces.AsyncTask;
import me.cobeine.sumo.utils.data.impl.InventoryFile;
import org.bukkit.entity.Player;

public class InventoryManager implements InventorySaver {


    @Override
    public void save(Player player) {
        AsyncTask.run(() -> new InventoryFile(player).createNew());
    }

    @Override
    public void load(Player player) {
        AsyncTask.run(() -> new InventoryFile(player).load(player));

    }
}
