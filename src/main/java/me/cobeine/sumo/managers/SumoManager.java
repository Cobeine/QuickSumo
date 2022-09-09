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
import me.cobeine.sumo.utils.Interfaces.Callback;
import me.cobeine.sumo.utils.data.impl.LocationsFile;
import me.cobeine.sumo.utils.enums.Chat;
import me.cobeine.sumo.utils.enums.GameState;
import me.cobeine.sumo.utils.enums.LocationType;
import me.cobeine.sumo.utils.rounds.Round;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SumoManager implements GameManager{
    private final Set<Player> players;
    private final HashMap<UUID,Location> last_location;
    private GameState gameState;
    private int minPlayers, maxPlayers,countdown;
    private Round currentRound;
    public SumoManager() {
        players = new HashSet<>();
        last_location = new HashMap<>();
        setup();
    }

    @Override
    public void setup() {
        setGameState(GameState.IDLE);
        minPlayers = Core.getConfigInt("Settings.min_players");
        maxPlayers = Core.getConfigInt("Settings.max_players");
        countdown = Core.getConfigInt("Settings.count_down");
    }

    @Override
    public void begin(Player player) {
        broadcastStart(player);
        setGameState(GameState.STARTING);
        preStart(() -> postStart(this::startNewRound));
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
        players.add(player);
        alert(Chat.color("Broadcasts.sumo_join").replace("{player}",player.getName()),false);
        last_location.put(player.getUniqueId(), player.getLocation());
        player.teleport(LocationsFile.getInstance().getLocation(LocationType.ARENA_SPAWN));
    }

    @Override
    public void postStart(Callback callback) {
        setGameState(GameState.FIGHTING);
        alert(Chat.color("Broadcasts.tournament_start"),false);
    }

    @Override
    public void startNewRound() {
        //Temporary method of getting players, will replace it with something better next time
        List<Player> arrayOfPlayers = new ArrayList<>(players);
        Collections.shuffle(arrayOfPlayers);
        Player player1 = arrayOfPlayers.get(0), player2 = arrayOfPlayers.get(1);
        this.currentRound = new Round(player1, player2);
        currentRound.onStart();

    }

    @Override
    public void end(Callback callback) {

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


}
