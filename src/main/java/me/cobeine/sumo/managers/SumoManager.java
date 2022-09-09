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

import lombok.Getter;
import me.cobeine.sumo.utils.Interfaces.Callback;
import me.cobeine.sumo.utils.enums.GameState;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
@Getter
public class SumoManager implements GameManager{
    private final Set<Player> players;
    private GameState gameState;
    public SumoManager() {
        players = new HashSet<>();
        setup();
    }

    @Override
    public void setup() {
        setGameState(GameState.IDLE);
    }

    @Override
    public void begin() {

    }

    @Override
    public void preStart(Callback callback) {

    }

    @Override
    public void join(Player player) {

    }

    @Override
    public void postStart(Callback callback) {

    }

    @Override
    public void startNewRound(Callback callback) {

    }

    @Override
    public void endRound(Callback callback) {

    }

    @Override
    public void end(Callback callback) {

    }

    @Override
    public void setGameState(GameState state) {
        this.gameState = state;
    }

    @Override
    public boolean canStart() {
        return false;
    }

}
