package me.cobeine.sumo.listeners;

import me.cobeine.sumo.Core;
import me.cobeine.sumo.utils.enums.GameState;
import me.cobeine.sumo.utils.rounds.Round;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class OtherListeners implements Listener {

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

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!Core.getInstance().getGameManager().getGameState().equals(GameState.STARTING))
            return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        if (event.getItem() == null)
            return;
        if (!event.getItem().equals(Core.leave_item))
            return;
        //TODO make leave command & leave method
    }

}
