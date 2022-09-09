package me.cobeine.sumo.managers;

import org.bukkit.entity.Player;

public interface InventorySaver {

    void save(Player player);

    void load(Player player);

}
