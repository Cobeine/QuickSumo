package me.cobeine.sumo.listeners;

import me.cobeine.sumo.Core;
import me.cobeine.sumo.utils.rounds.Round;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof Player))
            return;

        Player victim = ((Player) event.getEntity());
        Player damage = ((Player) event.getDamager());
        Round currentRound = Core.getInstance().getGameManager().getCurrentRound();
        if (currentRound.contains(victim) && currentRound.contains(damage))
            event.setDamage(0);
    }


}
