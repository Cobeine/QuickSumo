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
package me.cobeine.sumo.managers.impl;

import me.cobeine.sumo.Core;
import me.cobeine.sumo.managers.GameManager;
import me.cobeine.sumo.utils.Interfaces.AsyncTask;
import me.cobeine.sumo.utils.Interfaces.Callback;
import me.cobeine.sumo.utils.PlayerCache;
import me.cobeine.sumo.utils.data.impl.LocationsFile;
import me.cobeine.sumo.utils.Chat;
import me.cobeine.sumo.utils.enums.GameState;
import me.cobeine.sumo.utils.enums.LocationType;
import me.cobeine.sumo.utils.rounds.Round;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SumoManager implements GameManager {
    private final Set<Player> players;
    private final Set<PlayerCache> playerCaches;
    private GameState gameState;
    private int minPlayers, maxPlayers,countdown;
    private int y_death; //required Y coordinate to lose
    private Round currentRound;
    public SumoManager() {
        players = new HashSet<>();
        playerCaches = new HashSet<>();
        setup();
    }

    @Override
    public void setup() {
        setGameState(GameState.IDLE);
        minPlayers = Core.getConfigInt("Settings.min_players");
        maxPlayers = Core.getConfigInt("Settings.max_players");
        countdown = Core.getConfigInt("Settings.count_down");
        y_death = LocationsFile.getInstance().getLocation(LocationType.OPPONENT_ONE).getBlockY() -1;
        //OPPONENT_ONE or OPPONENT_TWO doesn't matter, sumo maps are flat
    }

    @Override
    public void begin(Player player) {
        AsyncTask.run(()->{
            broadcastStart(player);
            setGameState(GameState.STARTING);
            preStart(() -> postStart(this::startNewRound));
        });
    }

    @Override
    public void preStart(Callback callback) {
        new BukkitRunnable() {
            public void run() {
                sendActionbar(Chat.color("Actionbar.waiting"));
                if (players.size() < minPlayers)
                    return;
                //waiting for players
                countdown-=1;
                if (countdown % 10 == 0 || countdown < 5 && countdown > 0){
                    alert(Chat.color("Messages.starting_in").replace("{seconds}",
                            String.valueOf(countdown)),false);
                }
                if (countdown == 0){
                    this.cancel();
                    callback.call();
                }
            }
        }.runTaskTimer(Core.getInstance(), 1, 20);
    }

    @Override
    public void join(Player player) {
       AsyncTask.run(()->{
           players.add(player);
           Core.getInstance().getInventorySaver().save(player);
           alert(Chat.color("Broadcasts.sumo_join").replace("{player}",player.getName()),false);
           getPlayerCache(player);
           player.teleport(LocationsFile.getInstance().getLocation(LocationType.ARENA_SPAWN));
           player.setGameMode(GameMode.ADVENTURE);
       });
    }
    @Override
    public void leave(Player player){
        player.getInventory().clear();
        players.remove(player);
        endRound(player,true);
        restore(player);
    }
    @Override
    public void postStart(Callback callback) {
        setGameState(GameState.FIGHTING);
        alert(Chat.color("Broadcasts.tournament_start"),false);
    }

    @Override
    public void startNewRound() {
        //Temporary method of getting players, will replace it with something better next time
        AsyncTask.run(()->{
            List<Player> arrayOfPlayers = new ArrayList<>(players);
            Collections.shuffle(arrayOfPlayers);
            Player player1 = arrayOfPlayers.get(0), player2 = arrayOfPlayers.get(1);
            setCurrentRound(new Round(player1, player2));
            currentRound.onStart();
        });

    }

    @Override
    public void end() {
        for (PlayerCache playerCache : getCache()) {
            Player player = Bukkit.getPlayer(playerCache.getUuid());
            if (player == null)
                return;
            restore(player);
        }
    }
    @Override
    public void restore(Player player){
        Core.getInstance().getInventorySaver().load(player);
        PlayerCache cache = getPlayerCache(player);
        cache.restore(player);
        getCache().removeIf(e -> e.getUuid().equals(player.getUniqueId()));
    }


    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public boolean canStart() {
        for (LocationType type : LocationType.values()){
            if (LocationsFile.getInstance().getLocation(type) == null)
                return false;
        }
        return true;
    }

    @Override
    public boolean notFighting() {
        return !getGameState().equals(GameState.FIGHTING);
    }

    @Override
    public Integer getMinPlayers() {
        return minPlayers;
    }

    @Override
    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public Integer getCountdown() {
        return countdown;
    }
    @Override
    public void setGameState(GameState state) {
        this.gameState = state;
    }

    @Override
    public Set<Player> getPlayers() {
        return players;
    }

    @Override
    public Round getCurrentRound() {
        return currentRound;
    }

    @Override
    public void setCurrentRound(Round round) {
        this.currentRound = round;
    }

    @Override
    public Set<PlayerCache> getCache() {
        return playerCaches;
    }

    @Override
    public Integer getYDeath() {
        return y_death;
    }
}
