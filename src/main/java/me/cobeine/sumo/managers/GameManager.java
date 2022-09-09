/**
 * MIT License
 * <p>
 * Copyright (c) 2022 Cobeine
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. YOU ARE NOT ALLOWED TO RE-DISTRIBUTE AND/OR REPUBLISH. YOU ARE NOT ALLOWED TO FORK
 * UNLESS GIVEN CREDIT TO THE ORIGINAL AUTHOR (COBEINE)
 */
package me.cobeine.sumo.managers;

import me.cobeine.sumo.Core;
import me.cobeine.sumo.utils.Actionbar;
import me.cobeine.sumo.utils.Interfaces.Callback;
import me.cobeine.sumo.utils.Chat;
import me.cobeine.sumo.utils.enums.GameState;
import me.cobeine.sumo.utils.rounds.Round;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

public interface GameManager {

    void setup();

    void begin(Player player);
    void preStart(Callback callback);

    void join(Player player);

    void postStart(Callback callback);

    void startNewRound();

   // void endRound(Callback callback);

    void end(Callback callback);

    void setGameState(GameState state);

    GameState getGameState();

    boolean canStart();

    default void alert(String message, boolean isPublic) {
        if (isPublic)
            Bukkit.getServer().broadcastMessage(message);
        else getPlayers().forEach(player -> player.sendMessage(message));
    }

    default void broadcastStart(Player player) {
        String broadcast = Chat.coloredList(("Broadcasts.tournament_started"))
                .replace("{player}", player.getName());
        TextComponent component = new TextComponent(broadcast);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sumo join"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&',Core.getConfigString("Messages.click_hover"))
        )));
        Bukkit.getServer().spigot().broadcast(component);
    }

    default void sendActionbar(String message) {
        getPlayers().forEach(player -> Actionbar.send(player,message));
    }

    Integer getMinPlayers();

    Integer getMaxPlayers();

    Integer getCountdown();
    Set<Player> getPlayers();

    Round getCurrentRound();

    void setCurrentRound(Round round);

}
