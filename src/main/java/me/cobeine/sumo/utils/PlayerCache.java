package me.cobeine.sumo.utils;

import lombok.Getter;
import lombok.Setter;
import me.cobeine.sumo.Core;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;
@Setter
@Getter
public class PlayerCache {
    UUID uuid;
    Location last_location;
    GameMode previous_gameMode;
    public PlayerCache(Player player) {
        this.uuid = player.getUniqueId();
        setLast_location(player.getLocation());
        setPrevious_gameMode(player.getGameMode());
        Core.getInstance().getGameManager().getCache().add(this);
    }


    public void restore(Player player) {
        if (last_location == null)
            return;
        player.teleport(last_location);
        player.setGameMode(previous_gameMode);
    }
}
