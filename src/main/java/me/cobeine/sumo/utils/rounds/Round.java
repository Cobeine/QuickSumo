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
package me.cobeine.sumo.utils.rounds;

import me.cobeine.sumo.Core;
import me.cobeine.sumo.utils.data.impl.LocationsFile;
import me.cobeine.sumo.utils.Chat;
import me.cobeine.sumo.utils.enums.LocationType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Round {
    final Player player1,player2; //will probably replace them with array of players instead(low chance, but possible)
    final boolean final_round;
    public Round(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.final_round = Core.getInstance().getGameManager().getPlayers().size() == 2;
    }
    public void onStart() {
        player1.teleport(LocationsFile.getInstance().getLocation(LocationType.OPPONENT_ONE));
        player2.teleport(LocationsFile.getInstance().getLocation(LocationType.OPPONENT_TWO));
        new BukkitRunnable() {
            int countdown = Core.getConfigInt("Settings.round_count_down");
            final String countdown_message = Chat.color("Messages.round_count_down");
            final String round_started = Chat.color("Messages.round_started");
            public void run() {
                String msg = countdown_message.replace("{seconds}", String.valueOf(countdown));
                player1.sendMessage(msg);
                player2.sendMessage(msg);
                if (countdown == 0) {
                    this.cancel();
                    player1.sendMessage(round_started);
                    player2.sendMessage(round_started);
                }
                countdown-=1;
            }
        }.runTaskTimer(Core.getInstance(), 1, 20);
    }

    public void onEnd(Player loser) {
        Player winner = getOpponent(loser);
        Core.getInstance().getGameManager().alert(
                Chat.color("Broadcasts.round_win").replace("{winner}", winner.getName())
                        .replace("{loser}", loser.getName()), false);
        Core.getInstance().getGameManager().getPlayers().remove(loser);
        if (final_round){
            Core.getInstance().getGameManager().alert(Chat.color("Broadcasts.tournament_win")
                    .replace("{player}",winner.getName()),false);
            Core.getInstance().getGameManager().end();
            return;
        }
        new BukkitRunnable() {
            public void run() {
                Core.getInstance().getGameManager().startNewRound();
            }
        }.runTaskLater(Core.getInstance(), Core.getConfigInt("Settings.round_delay"));
    }

    public boolean contains(Player quitter) {
        return player1.equals(quitter) || player2.equals(quitter);
    }

    public Player getOpponent(Player quitter) {
        return player1.equals(quitter) ? player2 : quitter;
    }

}
